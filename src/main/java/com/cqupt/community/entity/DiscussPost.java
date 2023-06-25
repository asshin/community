package com.cqupt.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zsw
 * @create 2023-06-09 14:13
 */
@Data
public class DiscussPost {
    private  int id;
    private  int userId;
    private  String title;
    private  String content;
    private  int type;
    private  int status;
    private Date createTime;
    private  int commentCount;
    private  double score;
}
