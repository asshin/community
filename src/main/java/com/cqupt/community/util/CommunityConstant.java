package com.cqupt.community.util;

/**
 * @author zsw
 * @create 2023-06-26 19:34
 */
public interface CommunityConstant {
    /*
    * 激活成功
    * */
    int ACTIVATION_SUCCESS=0;
    /*
    * 重复激活
    * */
    int ACTIVATION_REPEAT=1;
    /*
    * 激活失败
    * */
    int ACTIVATION_FAILURE=2;

    /*
    * 默认状态的登录凭证的超时时间
    * */
    int DEFAULT_EXPIRED_SECONDS=3600*12;

    /*
     * 记住状态的登录凭证的超时时间
     * */
    int REMEMBER_EXPIRED_SECONDS=3600*12;

    /*
    * 实体类型：帖子
    * */

    int ENTITY_TYPE_POST=1;

    int ENTITY_TYPE_COMMENT=2;
    /*
    * */
    int ENTITY_TYPE_User=3;


}
