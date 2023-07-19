package com.cqupt.community;

import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.elasticsearch.MyDiscussPostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//import com.cqupt.community.dao.LoginTicketMapper;

/**
 * @author zsw
 * @create 2023-06-09 14:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticSearchTest {
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    private MyDiscussPostRepository discussPostRepo;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;



    @Test
    public void testInsert(){
        discussPostRepo.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepo.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepo.save(discussPostMapper.selectDiscussPostById(243));
//        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
//        System.out.println(loginTicket.getTicket()+":"+loginTicket.getStatus());
//        loginTicketMapper.updateStatus(loginTicket.getTicket(),1);
//        System.out.println(loginTicket.getTicket()+":"+loginTicket.getStatus());
    }
}
