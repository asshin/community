package com.cqupt.community.event;

import com.alibaba.fastjson.JSONObject;
import com.cqupt.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zsw
 * @create 2023-07-17 11:10
 */
@Component
public class EventProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //處理事件
    public void fireEvent(Event event){
        //将时间发布到指定主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));

    }



}
