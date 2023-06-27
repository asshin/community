package com.cqupt.community;

import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.List;

/**
 * @author zsw
 * @create 2023-06-09 14:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
    @Autowired
    MailClient mailClient;
    @Autowired
    TemplateEngine templateEngine;
    @Test
    public  void testTextMail(){
         mailClient.sendMail("857541619@qq.com","TEST","welcome.");
        }
    @Test
    public  void testHtmlMail(){
        Context context =new Context();
        context.setVariable("username","wang");
        String content = templateEngine.process("mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("857541619@qq.com","html",content);
    }
}
