package com.cqupt.community.dao;

import com.cqupt.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zsw
 * @create 2023-06-09 14:16
 */
@Mapper
public interface DiscussPostMapper {
        List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);
        //@param用于给参数取别名，如果只有一个参数且用在<if>里面，则必须起别名。
        int selectDiscussPostRows(@Param("userId") int userId);
        int insertDiscussPost(DiscussPost discussPost);


    DiscussPost selectDiscussPostById(int id);
}
