package com.cqupt.community.controller;

import com.cqupt.community.entity.User;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.RedisKeyUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zsw
 * @create 2023-06-25 22:10
 */
@Controller

public class LoginController implements CommunityConstant {
    private  static  final Logger logger=LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer producer;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${server.servlet.context-path}")
    private  String contextPath;
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    String getRegisterPage(){
        return  "/site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public  String login(){

        return "/site/login";
    }
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public  String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if (map==null||map.isEmpty()){
             model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活");
             model.addAttribute("target","/index");
             return  "/site/operate-result";

        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public  String activation (Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result =userService.activation(userId,code);
        if (result==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号已经可以正常使用了！");
            model.addAttribute("target","/login");

        }else if(result==ACTIVATION_REPEAT) {
            model.addAttribute("msg","无需操作，您的账号已经激活过了！");
            model.addAttribute("target","/login");
        }else if(result==ACTIVATION_FAILURE) {
            model.addAttribute("msg","激活失败,您提供的激活码不正确");
            model.addAttribute("target","/index");
        }
        return  "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public  void getKaptch(HttpServletResponse response/*,HttpSession session*/){
       //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        //验证码的归属
        String kaptchaOwner = CommunityUtil.gennerateUUid();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        //将验证码存入Redis
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptchaKey,text,60, TimeUnit.SECONDS);
//        //将验证码存入session
//        session.setAttribute("kaptcha",text);
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败："+e.getMessage());
        }

    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public  String login(String username,String password,String code,boolean rememberme,Model model/*,HttpSession session*/,@CookieValue("kaptchaOwner") String kaptchaOwner,HttpServletResponse response){
        //检查验证码
//        String kaptcha = (String)session.getAttribute("kaptcha");
        String kaptcha=null;
        if (StringUtils.isBlank(kaptchaOwner)){
            String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha =(String) redisTemplate.opsForValue().get(kaptchaKey);
        }


        if (StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确！");
            return  "/site/login";
        }
        //检查账号，密码
        int expiredSenconds=rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map =userService.login(username,password,expiredSenconds);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSenconds);
            response.addCookie(cookie);
            return  "redirect:/index";

        }else {

            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }

    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public  String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";


    }
}
