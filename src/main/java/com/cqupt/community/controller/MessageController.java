package com.cqupt.community.controller;

import com.cqupt.community.entity.Message;
import com.cqupt.community.entity.Page;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.MessageService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.*;

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
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    public  String getMessageList(Model model, Page page){
//        Integer.valueOf("abc");
        User user = hostHolder.getUser();
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
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
    @RequestMapping(path = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,Page page,  Model model){
        User user = hostHolder.getUser();
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/detail");
        page.setRows(messageService.findLetterCount(conversationId));

        //私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        ArrayList<Map<String,Object>> letters = new ArrayList<>();
        if (letterList!=null){
            for (Message message : letterList) {

                HashMap<String , Object> map = new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userService.getUserById(message.getFromId()));
                letters.add(map);
            }

        }
        model.addAttribute("letters",letters);
        //私信目标
        model.addAttribute("target",getletterTarget(conversationId));

        //设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()){
            messageService.readMessage(ids);
        }
        return  "/site/letter-detail";
    }


    @RequestMapping(path = "/letter/send",method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter( String toName,String content){
//        Integer.valueOf("abc");
        User target = userService.getUserByName(toName);
        if (target==null){
            return CommunityUtil.getJsonString(1,"目标用户不存在");

        }
        Message message=new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId()<message.getToId()){
            message.setConversationId(message.getFromId()+"_"+message.getToId());
        }else {
            message.setConversationId(message.getToId()+"_"+message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return   CommunityUtil.getJsonString(0);
    }

    private  User getletterTarget(String conversationId){
        String []ids =conversationId.split("_");
        int d0=Integer.parseInt(ids[0]);
        int d1=Integer.parseInt(ids[1]);
        if (hostHolder.getUser().getId()==d0){
            return  userService.getUserById(d1);
        }else {
            return  userService.getUserById(d0);
        }
    }
    private  List<Integer> getLetterIds(List<Message> letterList){
         List<Integer> ids = new ArrayList<>();
         if (letterList!=null){
             for (Message message : letterList) {
                 if (hostHolder.getUser().getId()==message.getToId()&&message.getStatus()==0){
                     ids.add(message.getId());
                 }

             }
         }
         return  ids;
    }
}
