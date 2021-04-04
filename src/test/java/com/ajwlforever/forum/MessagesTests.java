package com.ajwlforever.forum;

import com.ajwlforever.forum.entity.Message;
import com.ajwlforever.forum.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class MessagesTests {
    @Autowired
    private MessageService messageService;

    @Test
    public void insertTest() {
        Message m1 = new Message().setFromId(2)
                .setToId(5).setConversationId("2_5")
                .setContent("你好啊！").setCreateTime(new Date());
        Message m2 = new Message().setFromId(5)
                .setToId(2).setConversationId("2_5")
                .setContent("嗯，你好！").setCreateTime(new Date());
        Message m3 = new Message().setFromId(2)
                .setToId(4).setConversationId("2_4")
                .setContent("你好啊！").setCreateTime(new Date());
        messageService.addMessage(m1);
        messageService.addMessage(m2);
        messageService.addMessage(m3);
    }
    @Test
    public void conversationsTest()
    {
        List<Message> list = messageService.findConversations(2,0,10);
        for(Message message : list) {
            System.out.println(message);
        }
        System.out.println(messageService.findConversationCount(2));
    }
    @Test
    public void MessagesTest() {
        List<Message> list = messageService.findMessages("2_5",0,2);
        for(Message message : list) {
            System.out.println(message);
        }
        System.out.println(messageService.findMessagesCount("2_5"));
        System.out.println(messageService.findMessagesUnreadCount(2,"2_5"));
    }
}
