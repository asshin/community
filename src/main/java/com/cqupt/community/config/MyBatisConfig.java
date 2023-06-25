package com.cqupt.community.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zsw
 * @create 2023-05-16 11:20
 */
@Configuration
@EnableTransactionManagement//开启事务
@MapperScan("com.cqupt.community.dao")
public class MyBatisConfig {


}
