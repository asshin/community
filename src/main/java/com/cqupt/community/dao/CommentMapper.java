package com.cqupt.community.dao;

import com.cqupt.community.entity.Comment;
import com.cqupt.community.util.CommunityConstant;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
 * @author zsw
 * @create 2023-07-04 16:21
 */
@Mapper
public interface CommentMapper {
   List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);
   int selectCountByEntity(int entityType,int entityId);
   int insertComment(Comment comment);
}
