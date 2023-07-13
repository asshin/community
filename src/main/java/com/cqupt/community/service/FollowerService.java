package com.cqupt.community.service;

import com.cqupt.community.entity.User;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.HostHolder;
import com.cqupt.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zsw
 * @create 2023-07-10 12:58
 */
@Service
public class FollowerService implements CommunityConstant {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    public  void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getPrefixFollowerKey(entityType, entityId);
                System.out.println("-------");
                System.out.println(followeeKey);
                System.out.println("-------");
                System.out.println(followerKey);
                System.out.println("-------");
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
    //查询某用户关注的人
    public List<Map<String,Object>> findFollowees(int userId,int offset,int limit){
        String followeeUserKey = RedisKeyUtil.getPrefixFolloweeKey(userId, ENTITY_TYPE_User);
        Set<Integer> followeeIds = redisTemplate.opsForZSet().reverseRange(followeeUserKey, offset, offset + limit - 1);
        if (followeeIds==null){

            return  null;
        }
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (Integer followeeId : followeeIds) {
            HashMap<String , Object> map = new HashMap<>();
            User user = userService.getUserById(followeeId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followeeUserKey, followeeId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
         return list;
    }
    //查询关注某用户的粉丝
    public List<Map<String,Object>> findFollowers(int userId,int offset,int limit){
        String followerUserKey = RedisKeyUtil.getPrefixFollowerKey(ENTITY_TYPE_User,userId );
        Set<Integer> followerIds = redisTemplate.opsForZSet().reverseRange(followerUserKey, offset, offset + limit - 1);
        if (followerIds==null){

            return  null;
        }
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (Integer followerId : followerIds) {
            HashMap<String , Object> map = new HashMap<>();
            User user = userService.getUserById(followerId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followerUserKey, followerId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }
}
