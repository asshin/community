package com.cqupt.community.controller;

import com.cqupt.community.entity.Message;
import com.cqupt.community.entity.Page;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.MessageService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsw
 * @create 2023-07-06 11:31
 */
@Controller
@RequestMapping
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;


    //私信列表
    @RequestMapping(path = "/message/list",method = RequestMethod.GET)
    public  String getMessageList(Model model, Page page){
        User user = hostHolder.getUser();
        //分页信息
        page.setLimit(5);
        page.setPath("/message/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        //会话列表
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        ArrayList<Map<String,Object>> conversations = new ArrayList<>();
        if (conversationList!=null){
            for (Message message : conversationList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("conversation",message);
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
                int targetId= user.getId()==message.getFromId() ? message.getToId():message.getFromId();
                User target = userService.getUserById(targetId);
                map.put("target",target);
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);

        //查询总的未读消息数量
        int  letterUnreadCount =messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);

        return "/site/letter";



    }
}
