package com.cqupt.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zsw
 * @create 2023-07-04 16:19
 */
@Data
public class Comment {
    private  int id;
    private  int userId;
    private  int entityType;
    private  int entityId;
    private  int  targetId;
    private  String content;
    private  int status;
    private Date CreateTime;

}
