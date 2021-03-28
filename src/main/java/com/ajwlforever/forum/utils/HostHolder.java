package com.ajwlforever.forum.utils;

import com.ajwlforever.forum.entity.User;
import org.springframework.stereotype.Component;

/**
 * 应用中保持登录用户
 */
@Component
public class HostHolder {
    private  ThreadLocal<User> Users = new ThreadLocal<User>();
    public void setUser(User user)
    {
        Users.set(user);
    }

    public User getUser()
    {
        return Users.get();
    }
    public  void clear()
    {
        Users.remove();
    }
}
