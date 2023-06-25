package com.cqupt.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zsw
 * @create 2023-06-09 16:05
 */
@Data
public class User {
    private  int id;
    private  String  username;
    private  String title;
    private  String salt;
    private  String email;
    private  int type;
    private  int status;
    private  String activationCode;
    private  String headerUrl;
    private  Date  createTime;
}
