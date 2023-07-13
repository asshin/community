package com.cqupt.community.util;

/**
 * @author zsw
 * @create 2023-07-07 11:46
 */
public class RedisKeyUtil {
    public static final String SPLIT=":";
    public static final String  PREFIX_ENTITY_LIKE="like:entity";
    public static final String  PREFIX_USER_LIKE="like:user";
    public static final String  PREFIX_FOLLOWEE="like:followee";//关注者
    public static final String  PREFIX_FOLLOWER="like:follower";//粉丝
    public static final String  PREFIX_KAPTCHA="kaptcha";//验证码
    //某个实体赞
    //like:entity:entityType:entityId->set(userId）
    public   static  String getEntityLikeKey(int entityType,int entityId){
        return  PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;

    }

    //某个用户的赞
    public  static  String getUserLikeKey(int userId){
        return      PREFIX_USER_LIKE+SPLIT+userId;
    }
    //某个用户关注的实体
    //followee:userId:entityType->zset(entityId,now）
    public  static  String getPrefixFolloweeKey(int userId,int entityType){
        return     PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }
    //某个实体拥有的粉丝
    //followee:entityType:entityId->zset(userId,now）
    public  static  String getPrefixFollowerKey(int entityType,int entityId){
        return     PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
    }

    //登录验证码
    public  static  String getKaptchaKey(String owner){
        return  PREFIX_KAPTCHA+SPLIT+owner;


    }
}
