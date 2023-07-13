package com.cqupt.community.controller;

import com.cqupt.community.entity.Page;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.FollowerService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author zsw
 * @create 2023-07-10 13:48
 */
@Controller
public class FollowController  implements CommunityConstant {
    @Autowired
    private FollowerService followerService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @RequestMapping(path="/follow",method = RequestMethod.POST)
    @ResponseBody
    public  String follow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followerService.follow(user.getId(),entityType,entityId);
        return CommunityUtil.getJsonString(0,"已关注");
    }
    @RequestMapping(path="/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public  String unfollow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followerService.unfollow(user.getId(),entityType,entityId);
        return CommunityUtil.getJsonString(0,"已取消关注");
    }

    @RequestMapping(path = "/followees/{userId}")
    public  String getFollowees(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.getUserById(userId);
        if (user==null){
            throw  new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followees"+userId);
        page.setRows((int) followerService.findFolloweeCount(userId,ENTITY_TYPE_User));
        List<Map<String, Object>> followees = followerService.findFollowees(userId, page.getOffset(), page.getLimit());
        if (followees!=null){
            for (Map<String, Object> followee : followees) {
                User u =(User) followee.get("user");
                followee.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
         model.addAttribute("users",followees);
        return  "/site/followee";
    }
    @RequestMapping(path = "/followers/{userId}")
    public  String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.getUserById(userId);
        if (user==null){
            throw  new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followers"+userId);
        page.setRows((int) followerService.findFollowerCount(ENTITY_TYPE_User,userId));
        List<Map<String, Object>> followers = followerService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (followers!=null){
            for (Map<String, Object> follower : followers) {
                User u =(User) follower.get("user");
                follower.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",followers);
        return  "/site/follower";
    }
    private  boolean hasFollowed(int userId){
        if (hostHolder.getUser()==null){
            return  false;
        }

        return  followerService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_User,userId);
    }

}
