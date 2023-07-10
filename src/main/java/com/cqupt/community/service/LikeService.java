package com.cqupt.community.service;

import com.cqupt.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
    public void like(int userId, int entityUserId, int entityType, int entityId) {

//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        System.out.println(entityLikeKey);
//        Boolean ismember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (ismember){
//            redisTemplate.opsForSet().remove(entityLikeKey,userId);
//        }else {
//            redisTemplate.opsForSet().add(entityLikeKey,userId);
//        }
//        Set members = redisTemplate.opsForSet().members(entityLikeKey);
//        System.out.println("-------------");
//        Iterator iterator = members.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//        System.out.println("-------------");

        //redis事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);//
                //查询操作要放在事务之外，因为事务提交前其中的操作不会真正执行
                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();//开启事务
                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();

            }
        });

    }

    //查询某实体（帖子和评论）点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {

        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) == true ? 1 : 0;
    }
    //查询某个用户获得的赞的数量
    public  int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
       Integer count= (Integer)redisTemplate.opsForValue().get(userLikeKey);
        return  count==null?0:count.intValue();
    }
}
