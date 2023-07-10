package com.cqupt.community.controller;

import com.cqupt.community.entity.User;
import com.cqupt.community.service.FollowerService;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zsw
 * @create 2023-07-10 13:48
 */
@Controller
public class FollowController {
    @Autowired
    private FollowerService followerService;
    @Autowired
    HostHolder hostHolder;
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
}
