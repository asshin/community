package com.cqupt.community.controller;

import com.cqupt.community.entity.Comment;
import com.cqupt.community.service.CommentService;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author zsw
 * @create 2023-07-05 20:28
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @RequestMapping(path = "/add/{discussPostId}",method = RequestMethod.POST )
    public  String addComment(@PathVariable ("discussPostId") int discussPostId, Comment comment){

         //TODO 未登录异常处理
           comment.setUserId(hostHolder.getUser().getId());
           comment.setStatus(0);
           comment.setCreateTime(new Date());
           commentService.addComment(comment);
           return  "redirect:/discuss/detail/"+discussPostId;


    }
}
