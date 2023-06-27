package com.cqupt.community.util;

import com.cqupt.community.entity.User;
import org.springframework.stereotype.Component;

/**持有用户信息，用于代替session对象
 * @author zsw
 * @create 2023-06-27 16:09
 */
@Component
public class HostHolder {
    private  ThreadLocal<User> users=new ThreadLocal<>();
    public  void setUser(User user){
        users.set(user);
    }
    public  User getUser(){
        return  users.get();
    }
    public void  clear(){
        users.remove();
    }
}
