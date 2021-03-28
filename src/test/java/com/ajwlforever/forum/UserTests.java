package com.ajwlforever.forum;


import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class UserTests {

    @Autowired
    private UserService userService;

    @Test
    public void insertTest()
    {
        User user = new  User();
        user.setId(2)
                .setUsername("ajwlforever")
                .setNickname("yqx")
                .setPassword("zjh5211314")
                .setSalt("asdasdasdasd")
                .setEmail("ajwlforever@163.com")
                .setType(1)
                .setActiveCode("Asdasdasd")
                .setStatus(0)
                .setCreateTime(new Date())
                .setActiveTime(new Date())
                .setHeaderUrl("www.baidu.coim");
        userService.insertUser(user);

        User user2 = userService.selectById(2);
        System.out.println(user2);

        User user3 = userService.selectByName("root");
        System.out.println(user3);

        User user4 = userService.selectByEmail("ajwlforever@163.com");
        System.out.println(user4);


    }

    @Test
    public void updateTest()
    {
        User user4 = userService.selectByEmail("ajwlforever@163.com");
        System.out.println(user4);
        userService.updateStatus(user4.getId(),1);
        userService.updateHeader(user4.getId(),"aaaaaaaaaaaa");
        userService.updatePassword(user4.getId(),"aaaaaaaaaa");

        System.out.println(userService.selectById(user4.getId()));

    }
}
