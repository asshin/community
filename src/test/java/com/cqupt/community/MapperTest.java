package com.cqupt.community;

import com.cqupt.community.CommunityApplication;
import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.dao.LoginTicketMapper;
import com.cqupt.community.entity.DiscussPost;

import com.cqupt.community.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.Log;

import java.util.Date;
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
    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Test
    public  void discussPostTest(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        int i = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(i);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
    }
    @Test
    public  void LoginTicketTest(){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void LoginTicketSelectTest(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket.getTicket()+":"+loginTicket.getStatus());
        loginTicketMapper.updateStatus(loginTicket.getTicket(),1);
        System.out.println(loginTicket.getTicket()+":"+loginTicket.getStatus());
    }
}
