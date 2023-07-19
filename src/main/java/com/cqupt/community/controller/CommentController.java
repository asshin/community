package com.cqupt.community.controller;

import com.cqupt.community.entity.Comment;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Event;
import com.cqupt.community.event.EventProducer;
import com.cqupt.community.service.CommentService;
import com.cqupt.community.service.DiscussPostService;
import com.cqupt.community.util.CommunityConstant;
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
public class CommentController  implements CommunityConstant {
    @Autowired
    CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    DiscussPostService discussPostService;

    @RequestMapping(path = "/add/{discussPostId}",method = RequestMethod.POST )
    public  String addComment(@PathVariable ("discussPostId") int discussPostId, Comment comment){

         //TODO 未登录异常处理
           comment.setUserId(hostHolder.getUser().getId());
           comment.setStatus(0);
           comment.setCreateTime(new Date());
           commentService.addComment(comment);

           //触发评论事件
        Event event = new Event();
        event.setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setData("postId",discussPostId);
        if (comment.getEntityType()==ENTITY_TYPE_POST){
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType()==ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);
        if (comment.getEntityType()==ENTITY_TYPE_POST){
       event = new Event()
                    .setTopic(TOPIC_DiscussPost)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }
        return  "redirect:/discuss/detail/"+discussPostId;


    }
}
