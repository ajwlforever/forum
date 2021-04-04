package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.entity.Message;
import com.ajwlforever.forum.entity.Page;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.MessageService;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.*;

/**
 *  消息的控制器
 */
@Controller
public class MessageController implements ForumConstant {

    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    //只显示会话和系统消息
    @GetMapping({"/messages","/letters"})
    public String getMessages(Model model,Page page)
    {
        User hostUser = hostHolder.getUser();
        // 左侧会话信息
        //一开始只显示这些会话，点击更多的post请求动态添加会话
        List<Message> conversations = messageService.findConversations(hostUser.getId(),0,10);
        model.addAttribute("conversationsMessages",messageService.getConversationsMessages(conversations));
        //系统消息显示。
        page.setPath("/messages");
        page.setLimit(PAGE_MESSAGE_LIMIT);
        int noticeCount = messageService.selectNoticeCount(hostUser.getId());
        page.setRows(noticeCount);
        List<Message> notices = messageService.selectAllNotices(hostUser.getId(),page.getOffset(),page.getLimit());
        model.addAttribute("noticesInfo",messageService.getNoticesInfo(notices));

        model.addAttribute("opposeUser",userService.selectById(1));

        return "messages-page";
    }

    @GetMapping("/message/{conversationId}")
    public String getMessage(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        User hostUser = hostHolder.getUser();
        // 左侧会话信息
        //一开始只显示这些会话，点击更多的post请求动态添加会话
        List<Message> conversations = messageService.findConversations(hostUser.getId(),0,10);
        model.addAttribute("conversationsMessages",messageService.getConversationsMessages(conversations));
        //系统消息不显示。

        //右侧消息列表
        page.setLimit(PAGE_MESSAGE_LIMIT);
        int rows = messageService.findMessagesCount(conversationId);
        page.setRows(rows);
        page.setPath("/message/"+conversationId);
        //消息的具体信息
        List<Message> messages = messageService.findMessages(conversationId,page.getOffset(), page.getLimit());
        model.addAttribute("messagesInfo",messageService.getMessagesInfo(messages));

        model.addAttribute("opposeUser",userService.selectById(getTarget(hostUser.getId(),conversationId)));

        return "messages-page";
    }

    @PostMapping("/create_message")
    @ResponseBody
    public String createMessage(Message message){
        if(StringUtils.isBlank(message.getContent())||message.getContent().length()>20){
            return ForumUtils.toJsonString(1,"私信内容长度有问题");
        }
        User hostUser = hostHolder.getUser();
        message.setFromId(hostUser.getId())
                .setToId(getTarget(hostUser.getId(),message.getConversationId()))
                .setStatus(MESSAGE_STATUS_UNREAD)
                .setCreateTime(new Date())
        ;
        messageService.addMessage(message);
        return ForumUtils.toJsonString(0,"私信发送成功!");
    }
    public int getTarget(int id,String conversationId){
        String[] ids = conversationId.split("_");
        int id1 = Integer.parseInt(ids[0]);
        int id2 = Integer.parseInt(ids[1]);
        return id1 == id? id2:id1;
    }
}
