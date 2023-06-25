package com.cqupt.community.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsw
 * @create 2023-06-06 16:23
 */
@RestController
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/hello")
    public  String sayhello(){
        return "hello";
    }

}
