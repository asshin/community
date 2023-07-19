package com.cqupt.community.controller;

import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.Page;
import com.cqupt.community.service.ElasticsearchService;
import com.cqupt.community.service.LikeService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zsw
 * @create 2023-07-19 15:17
 */
@Controller
public class SearchController implements CommunityConstant {
    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public  String search(String keyword, Page page, Model model){
        //搜索帖子


        org.springframework.data.domain.Page<DiscussPost> searchresult = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        
        //聚合數據
        ArrayList<Map<String,Object>> discussPosts = new ArrayList<>();
        if (searchresult!=null){
            for (DiscussPost post : searchresult) {
                HashMap<String, Object> map = new HashMap<>();
                //帖子
                map.put("post",post);
                //作者
                map.put("user",userService.getUserById(post.getUserId()));
                //點贊數量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));
                discussPosts.add(map);
            }

        }
             model.addAttribute("discussPosts",discussPosts);
             model.addAttribute("keyword",keyword);
             //分頁信息
        page.setPath("/search?keyword=" +keyword);
        page.setRows(searchresult ==null?0:(int)searchresult.getTotalElements());
        return  "/site/search";
    }
}
