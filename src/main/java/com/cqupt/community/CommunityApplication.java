package com.cqupt.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class CommunityApplication {

    @PostConstruct
    public  void init(){
//        System.setProperty("es.set.netty.runtime.available.processors","false");
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
