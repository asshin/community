package com.cqupt.community.service;

import com.cqupt.community.dao.UserMapper;
import com.cqupt.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zsw
 * @create 2023-06-09 16:00
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public User getUserById(int id){
        return userMapper.getUserById(id);
    }
}
