package com.cqupt.community.service;

import com.cqupt.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

/**
 * @author zsw
 * @create 2023-07-07 11:53
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public   void like(int userId,int entityType,int entityId){

        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        System.out.println(entityLikeKey);
        Boolean ismember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (ismember){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }else {
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }
        Set members = redisTemplate.opsForSet().members(entityLikeKey);
        System.out.println("-------------");
        Iterator iterator = members.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("-------------");

    }
    //查询某实体（帖子和评论）点赞的数量
    public  long findEntityLikeCount(int  entityType,int entityId){

        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return  redisTemplate.opsForSet().size(entityLikeKey);
    }
    //查询某人对某实体的点赞状态
    public  int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return  redisTemplate.opsForSet().isMember(entityLikeKey,userId)==true?1:0;
    }
}
