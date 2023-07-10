package com.cqupt.community.controller;

import com.cqupt.community.entity.User;
import com.cqupt.community.service.LikeService;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author zsw
 * @create 2023-07-07 12:06
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public  String like(int entityType,int entityId,int entityUserId){
        User user = hostHolder.getUser();
            if (user==null){
               throw  new NullPointerException("用户为登录");
            }
            //点赞
            likeService.like(user.getId(),entityUserId,entityType,entityId);
            //数量
            long likeCount =likeService.findEntityLikeCount(entityType,entityId);
            //状态
            int likestatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("likeCount",likeCount);
            map.put("likeStatus",likestatus);
            return CommunityUtil.getJsonString(0,null,map);



    }
}
