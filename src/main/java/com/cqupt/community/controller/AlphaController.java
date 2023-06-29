package com.cqupt.community.controller;

import com.cqupt.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zsw
 * @create 2023-06-06 16:23
 */
@Controller
@RequestMapping("alpha")
public class AlphaController {
    @RequestMapping("/hello")
    public  String sayhello(){
        return "hello";
    }
    //cookie 实例
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public  String getcookie(HttpServletResponse response){
        Cookie cookie = new Cookie("username","wang");
         //设置cookie的生效范围
        cookie.setPath("/community/alpha");
        //设置cookie的生效时间，一旦设置之后就会存在硬盘里面
        cookie.setMaxAge(60*10);
        //发送cookie
        response.addCookie(cookie);
        return " set cookie";

    }
    //cookie 实例
    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public  String testcookie(@CookieValue("username") String username){

        return username;

    }

    //ajax示例
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)

    public  String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return  "sucess";
    }
}
