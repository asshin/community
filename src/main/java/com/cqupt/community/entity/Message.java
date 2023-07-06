package com.cqupt.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zsw
 * @create 2023-07-06 10:24
 */
@Data
public class Message {
    private  int id;
    private  int fromId;
    private  int toId;
    private  String conversationId;
    private  String  content;
    private  int status;
    private Date createTime;

}
