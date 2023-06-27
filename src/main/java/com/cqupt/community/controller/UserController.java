package com.cqupt.community.controller;

import com.cqupt.community.entity.User;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zsw
 * @create 2023-06-27 16:44
 */
@Controller
@RequestMapping("/user")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private  String uploadPath;
    @Value("${community.path.domain}")
    private  String domain;
    @Value("${server.servlet.context-path}")
    private  String contextPath;

    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(value = "/setting",method = RequestMethod.GET)
    public  String getSettingPage(){
        return  "/site/setting";
    }
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage==null){
            model.addAttribute("error","你还没有选择图片");
            return "/site/setting";
        }
        String fileName=headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
        }
        //生成随机文件名
        fileName= CommunityUtil.gennerateUUid()+suffix;
        //确定文件存放的路径
         File dest =new File(uploadPath+"/"+fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败:"+e.getMessage());
            throw new RuntimeException("文件上传失败，服务器发生异常",e);
        }
        //&#x66f4;&#x65b0;&#x5f53;&#x524d;&#x7528;&#x6237;&#x7684;&#x5934;&#x50cf;&#x8def;&#x5f84;&#xff08;web&#x8bbf;&#x95ee;&#x8def;&#x5f84;&#xff09;
        //http://localhost:8081/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl=domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);
        return  "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public  void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器存放路径
        fileName=uploadPath+"/"+fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);
        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(fileName);)
        {
                      //建立输出缓存区
            byte[] buffer = new byte[1024];
            int b;
            while ((b=fileInputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,b);
            }

        } catch (IOException e) {
            logger.error("读取头像失败："+e.getMessage());
        }finally {

        }


    }
}
