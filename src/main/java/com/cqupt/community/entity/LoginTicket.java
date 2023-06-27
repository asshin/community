package com.cqupt.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zsw
 * @create 2023-06-27 10:48
 */
@Data
public class LoginTicket {
    private  int id;
    private  int userId;
    private  String ticket;
    private  int status;
    private Date expired;

}
