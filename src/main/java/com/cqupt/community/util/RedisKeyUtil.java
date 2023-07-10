package com.cqupt.community.util;

/**
 * @author zsw
 * @create 2023-07-07 11:46
 */
public class RedisKeyUtil {
    public static final String SPLIT=":";
    public static final String  PREFIX_ENTITY_LIKE="like:entity";
    public static final String  PREFIX_USER_LIKE="like:user";
    //某个实体赞
    //like:entity:entityType:entityId->set(userId）
    public   static  String getEntityLikeKey(int entityType,int entityId){
        return  PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;

    }

    //某个用户的赞
    public  static  String getUserLikeKey(int userId){
        return      PREFIX_USER_LIKE+SPLIT+userId;
    }
}
