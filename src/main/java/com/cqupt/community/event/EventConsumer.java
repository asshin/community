package com.cqupt.community.event;

import com.alibaba.fastjson.JSONObject;
import com.cqupt.community.elasticsearch.MyDiscussPostRepository;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Event;

import com.cqupt.community.entity.Message;
import com.cqupt.community.service.DiscussPostService;
import com.cqupt.community.service.ElasticsearchService;
import com.cqupt.community.service.MessageService;
import com.cqupt.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zsw
 * @create 2023-07-17 11:10
 */
@Component
public class EventConsumer implements CommunityConstant {
     public static final Logger logger=LoggerFactory.getLogger(EventConsumer.class);

     @Autowired
     private MessageService messageService;
      @Autowired
      private DiscussPostService discussPostService;
      @Autowired
      private ElasticsearchService elasticsearchService;
     @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord record){
         if (record==null||record.value()==null){
             logger.error("消息内容为空");
             return;
         }
         Event event = JSONObject.parseObject(record.value().toString(), Event.class);
         if (event==null){
             logger.error("消息格式不对");
         }
         //发送站内通知
         Message message = new Message();
         message.setFromId(SYSTEM_USER_ID);
         message.setToId(event.getEntityUserId());
         message.setConversationId(event.getTopic());
         message.setCreateTime(new Date());
         //通知内容xx给你发送了一条消息点击查看xxxx
         HashMap<String, Object> content = new HashMap<>();
         content.put("userId",event.getUserId());
         content.put("entityType",event.getEntityId());
         content.put("entityId",event.getEntityId());

         if (!event.getData().isEmpty()){
             for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                 content.put(entry.getKey(),entry.getValue());

             }
         }
         message.setContent(JSONObject.toJSONString(content));
         messageService.addMessage(message);
     }

/*
* 消费发帖事件
* */
    @KafkaListener(topics = {TOPIC_DiscussPost})
    public void handlePublishMessage(ConsumerRecord record){
        if (record==null||record.value()==null){
            logger.error("消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event==null){
            logger.error("消息格式不对");
        }
        DiscussPost dicusspost = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(dicusspost);
        //发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        //通知内容xx给你发送了一条消息点击查看xxxx
        HashMap<String, Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityId());
        content.put("entityId",event.getEntityId());

        if (!event.getData().isEmpty()){
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(),entry.getValue());

            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }





}
