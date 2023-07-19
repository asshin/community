package com.cqupt.community;

import com.cqupt.community.util.MailClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author zsw
 * @create 2023-06-09 14:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTest {
   @Autowired
   private  KafkaProducer kafkaProducer;

    @Test
    public  void testKafka(){
        kafkaProducer.sendMsg("test","您好");
        kafkaProducer.sendMsg("test","在吗");
        try {
             Thread.sleep(10*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

@Component
class  KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;
    public  void sendMsg(String topic,String content){
        kafkaTemplate.send(topic,content);
    }
}
@Component
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public  void handleMsg(ConsumerRecord record){
//        System.out.println("----------");
        System.out.println(record.value());
//        System.out.println("----------");
    }
}
