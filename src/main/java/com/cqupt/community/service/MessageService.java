package com.cqupt.community.service;


import com.cqupt.community.dao.MessageMapper;
import com.cqupt.community.entity.Message;
import com.cqupt.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationsCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectMessageByConversation(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectMessageCountByConversation(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectMessageUnreadCount(userId, conversationId);
    }

//    public int addMessage(Message message) {
//        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
//        message.setContent(sensitiveFilter.filter(message.getContent()));
//        return messageMapper.insertMessage(message);
//    }

//    public int readMessage(List<Integer> ids) {
//        return messageMapper.updateStatus(ids, 1);
//    }

}
