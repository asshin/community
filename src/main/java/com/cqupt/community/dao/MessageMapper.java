package com.cqupt.community.dao;

import com.cqupt.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zsw
 * @create 2023-07-06 10:27
 */
@Mapper
public interface  MessageMapper {

    //查询当前用户的会话列表，针对每个会话只返回一条新的私信;
    List<Message> selectConversations(int userId,int offset,int limit);
    //查询当前用户的会话数量
    int selectConversationsCount(int userId);

    //查询某个会话所包含的私信列表
     List<Message> selectMessageByConversation(String conversationId ,int offset,int limit);
    //查询某个会话包含的私信数量
     int selectMessageCountByConversation(String conversationId);
    //查询未读私信的数量
    int  selectMessageUnreadCount(int userId,String conversationId);

    //增加私信
    int insertMessage(Message message);

    //更新消息状态
    int updateMessageStatus(List<Integer> ids,int status);

    //查询某天个主题下的最新通知
    Message selectLatestNotice(int userId,String topic);
    // 查询某个主题包含的通知数量
    int selectNoticeCount(int userId,String topic);
    //查询未读的通知数量
    int selectNoticeUnreadCount(int userId,String topic);

    //查询某个主题所含的通知列表
    List<Message> selectNotices(int userId,String topic,int offset,int limit);



}
