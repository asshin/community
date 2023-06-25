package com.cqupt.community;

import com.cqupt.community.CommunityApplication;
import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.entity.DiscussPost;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zsw
 * @create 2023-06-09 14:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Test
    public  void discussPostTest(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        int i = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(i);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
    }
}
