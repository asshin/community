package com.cqupt.community;

import com.cqupt.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zsw
 * @create 2023-06-27 22:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以操你妈,可以嫖娼,可以狗日的,可以开票,哈哈哈!";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "这里可以☆操☆你☆妈☆,可以☆嫖☆娼☆,可以☆狗☆日☆的☆,可以☆开☆票☆,哈哈哈!";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
