package com.cqupt.community.util;

/**
 * @author zsw
 * @create 2023-07-07 11:46
 */
public class RedisKeyUtil {
    public static final String SPLIT=":";
    public static final String  PREFIX_ENTITY_LIKE="like:entity";
    //某个实体赞
    //like:entity:entityType:entityId->set(userId）
    public   static  String getEntityLikeKey(int entityType,int entityId){
        return  PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;

    }
}
