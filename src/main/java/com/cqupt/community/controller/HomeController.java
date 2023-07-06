package com.cqupt.community.controller;

import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Page;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.DiscussPostService;
import com.cqupt.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsw
 * @create 2023-06-09 16:37
 */
@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public  String getIndexPage(Model model, Page page){
        // 方法调用之前，springmvc会自动实例化model和page，并将page注入model
        //所以在thmelef里面可以直接使用page对象
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        for (DiscussPost discussPost : list) {
            User user = userService.getUserById(discussPost.getUserId());
            HashMap<String , Object>  map = new HashMap<>();
            map.put("post",discussPost);
            map.put("user",user);
            discussPosts.add(map);

        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public  String getErrorPage(){
        return "/error/500";
    }

}
