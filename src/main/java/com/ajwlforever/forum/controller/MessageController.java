package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.entity.Message;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.MessageService;
import com.ajwlforever.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *  消息的控制器
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @GetMapping({"/messages","/letters"})
    public String getMessages(Model model)
    {
        User hostUser = hostHolder.getUser();
        // 会话信息
        int conversationCount = messageService.findConversationCount(hostUser.getId());
        
        return "messages-page";
    }

}
