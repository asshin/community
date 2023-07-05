package com.cqupt.community.controller;

import com.cqupt.community.entity.Comment;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Page;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.CommentService;
import com.cqupt.community.service.DiscussPostService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;

import org.quartz.impl.jdbcjobstore.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Array;
import java.sql.PseudoColumnUsage;
import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJsonString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public  String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //查询到帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);
       //查询作者名称
        User user = userService.getUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        //查评论的分页信息
        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());
        List<Comment> commentsList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
        //评论：给帖子的评论
        //回复：给评论的评论
        List<Map<String,Object>> commentVoList= new ArrayList<>();
        if (commentsList !=null){
          for(Comment  comment:commentsList){
          Map<String,Object>  commentVo=     new HashMap<>();
          commentVo.put("comment",comment);
          commentVo.put("user",userService.getUserById(comment.getUserId()));
          //评论的评论
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                //回复Vo列表
                List<Map<String,Object> >replyVoList= new ArrayList<>();
                if (replyList!=null){
                    for (Comment reply : replyList) {
                        HashMap<String, Object> replyVo = new HashMap<>();
                        //回复
                        replyVo.put("reply",reply);
                        //作者
                        replyVo.put("user",userService.getUserById(reply.getUserId()));
                        //回复目标
                        User target=reply.getTargetId()==0?null:userService.getUserById(reply.getTargetId());
                        replyVo.put("target",target);
                        replyVoList.add(replyVo);


                    }
                }
                commentVo.put("replys",replyVoList);
                //回复数量
              int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
              commentVo.put("replyCount",replyCount);
                commentVoList.add(commentVo);
            }


        }
        model.addAttribute("comments",commentVoList);
        return  "/site/discuss-detail";

    }

}
