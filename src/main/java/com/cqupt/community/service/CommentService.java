package com.cqupt.community.service;

import com.cqupt.community.dao.CommentMapper;
import com.cqupt.community.entity.Comment;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author zsw
 * @create 2023-07-04 16:33
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private  DiscussPostService discussPostService;
    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return  commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }
    public  int findCommentCount(int entityType,int entityId){
        return  commentMapper.selectCountByEntity(entityType,entityId);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public  int addComment(Comment comment){
        if (comment==null){
            throw  new IllegalArgumentException("参数不能能为空");
        }
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int  rows=commentMapper.insertComment(comment);
        //更新帖子的评论数
        if(comment.getEntityType()==ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),count);
        }
        return  rows;
    }
    public  Comment findCommentById(int id){
        return  commentMapper.findCommentById(id);
    }
}
