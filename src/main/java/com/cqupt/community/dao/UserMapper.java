package com.cqupt.community.dao;

import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zsw
 * @create 2023-06-09 14:16
 */
@Mapper
public interface UserMapper {
       User  getUserById(@Param("id") int id);
        //@param用于给参数取别名，如果只有一个参数且用在<if>里面，则必须起别名。


}
