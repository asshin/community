package com.cqupt.community.service;


import com.cqupt.community.dao.UserMapper;
import com.cqupt.community.entity.LoginTicket;
import com.cqupt.community.entity.User;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.MailClient;

import com.cqupt.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zsw
 * @create 2023-06-09 16:00
 */
@Service
public class UserService  implements CommunityConstant {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    RedisTemplate redisTemplate;
//    private   LoginTicketMapper loginTicketMapper;
    @Value("${server.servlet.context-path}")

    private  String contextPath;
    @Value("${community.path.domain}")
    private  String domain;

    public User getUserById(int id){
        User user = getCache(id);
        if (user==null){
            user=initCache(id);
        }
        return user;
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map =new HashMap<>();
        //空值处理
        if (user==null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("usernameMsg","账号不能为空!");
            return  map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("passwordMsg","密码不能为空!");
            return  map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空!");
            return  map;
        }

        //验证账号
        User u=userMapper.getUserByUsername(user.getUsername());
        if (u!=null){
            map.put("usernameMsg","该账号名已被用");
            return  map;
        }
        //验证邮箱
        u=userMapper.getUserByEmail(user.getEmail());
        if (u!=null){
            map.put("emailMsg","该邮箱已被注册");
            return  map;
        }
        //注册用户
        user.setSalt(CommunityUtil.gennerateUUid().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.gennerateUUid());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //激活邮件
        Context context =new Context();
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();//该id是由mybatis自动回填所得
        context.setVariable("url",url);
        String content =templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return  map;
    }

    public int activation(int userId,String code){
        User user =userMapper.getUserById(userId);
        if (user.getStatus()==1){
            return  ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            clearCache(userId);
            return  ACTIVATION_SUCCESS;
        }else {
            return  ACTIVATION_FAILURE;
        }
    }
    public  Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String ,Object> map=new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(password)){
            map.put("usernameMsg","账号不能为空！");
            return  map;

        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return  map;
        }
        //验证账号
        User user = userMapper.getUserByUsername(username);
        if (user==null){
            map.put("usernameMsg","该账号还未注册");
            return  map;
        }
        //验证激活状态
        if (user.getStatus()==0){
            map.put("usenameMsg","该状态未激活");
            return  map;
        }
        //验证密码
        password=CommunityUtil.md5(password+user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确!");
            return map;
        }
        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.gennerateUUid());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*expiredSeconds));
//        loginTicketMapper.insertLoginTicket(loginTicket);
        String rediskey= RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(rediskey,loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        String rediskey= RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket =(LoginTicket) redisTemplate.opsForValue().get(rediskey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(rediskey,loginTicket);
    }

    public  LoginTicket findLoginTicket(String ticket){

        return  (LoginTicket) redisTemplate.opsForValue().get(RedisKeyUtil.getTicketKey(ticket));
    }
    public  int updateHeader(int userId,String headerUrl){
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return   rows;
    }
    public  User getUserByName(String name){
        User user = userMapper.getUserByUsername(name);
        return  user;
    }

    //1.优先从缓存中取值
    private  User getCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return  (User)redisTemplate.opsForValue().get(redisKey);

    }
    //2.取不到时初始化缓存
    private  User initCache(int userId){
        User user = userMapper.getUserById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
       redisTemplate.opsForValue().set(redisKey,user,3600, TimeUnit.SECONDS);
        return user;
    }
    //3.数据变更时清除缓存
    private  void clearCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);

    }

    public Collection<? extends GrantedAuthority> getAuthorities(int userId){
        User user = this.getUserById(userId);
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()){
                    case 1:
                        return  AUTHORITY_ADMIN;
                    case 2:
                        return  AUTHORITY_MODERATOR;
                    default:
                        return  AUTHORITY_USER;
                }

            }
        });
        return list;


    }
}
