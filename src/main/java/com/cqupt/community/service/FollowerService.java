package com.cqupt.community.service;

import com.cqupt.community.util.HostHolder;
import com.cqupt.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author zsw
 * @create 2023-07-10 12:58
 */
@Service
public class FollowerService {
    @Autowired
    private RedisTemplate redisTemplate;

    public  void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getPrefixFollowerKey(entityType, entityId);

                operations.multi();;
                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());

                return operations.exec();




            }
        });
    }


    public  void unfollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getPrefixFollowerKey(entityType, entityId);

                operations.multi();
                operations.opsForZSet().remove(followeeKey,entityId);
                operations.opsForZSet().remove(followerKey,userId,System.currentTimeMillis());

                return operations.exec();




            }
        });
    }

    //查询关注的实体数量
    public  long findFolloweeCount(int userId,int entityType){

        String followeeKey =RedisKeyUtil.getPrefixFolloweeKey(userId,entityType);
        return  redisTemplate.opsForZSet().zCard(followeeKey);
    }
    //查询实体的粉丝数量
    public  long findFollowerCount(int entityType,int entityId){

        String followerKey =RedisKeyUtil.getPrefixFollowerKey(entityType,entityId);
        return  redisTemplate.opsForZSet().zCard(followerKey);
    }

    //查询当前用户是否以关注某实体
    public boolean hasFollowed(int userId,int entityType,int entityId){
        String prefixFolloweeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
        return  redisTemplate.opsForZSet().score(prefixFolloweeKey,entityId)!=null;
    }
}