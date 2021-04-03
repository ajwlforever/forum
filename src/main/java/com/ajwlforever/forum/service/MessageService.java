package com.ajwlforever.forum.service;

import com.ajwlforever.forum.dao.MessageMapper;
import com.ajwlforever.forum.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 消息的具体操作类
 */

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        return messageMapper.insertMessage(message);
    }
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }
}
