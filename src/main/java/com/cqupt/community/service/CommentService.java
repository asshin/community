package com.cqupt.community.service;

import com.cqupt.community.dao.CommentMapper;
import com.cqupt.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsw
 * @create 2023-07-04 16:33
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return  commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }
    public  int findCommentCount(int entityType,int entityId){
        return  commentMapper.selectCountByEntity(entityType,entityId);
    }
}
