package com.cqupt.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import javafx.beans.DefaultProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * @author zsw
 * @create 2023-06-27 9:55
 */
@Configuration
public class KaptchaConfig  {
   @Bean
    public  Producer kaptchaProducer(){
       Properties properties = new Properties();
       properties.setProperty("kaptcha.image.width","100");
       properties.setProperty("kaptcha.image.height","40");
       properties.setProperty("kaptcha.textproducer.font.color","0,0,0");
       properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
       properties.setProperty("kaptcha.textproducer.char.length","4");
       properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");


       DefaultKaptcha kaptcha = new DefaultKaptcha();
       Config config = new Config(properties);
       kaptcha.setConfig(config);
       return  kaptcha;

   }

}
