package com.ajwlforever.forum.service;

import com.ajwlforever.forum.dao.MessageMapper;
import com.ajwlforever.forum.entity.Message;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.HostHolder;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.logging.Level;

/**
 * 消息的具体操作类
 */

@Service
public class MessageService implements ForumConstant {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    //得到每一个会话具体的信息
    public List<Map<String, Object>> getConversationsMessages(List<Message> conversations)
    {
        User hostUser = hostHolder.getUser();
        List<Map<String, Object>> res = new ArrayList<>();
        if(conversations!=null){
            for(Message message:conversations) {
                Map<String ,Object> map  = new HashMap<>();
                map.put("message",message);
                int unread = findMessagesUnreadCount(hostUser.getId(),message.getConversationId());
                map.put("unreadCount",unread);
                if(hostUser.getId()==message.getFromId())
                {
                    map.put("fromWho","你");  //是自己发的
                    User user = userService.selectById(message.getToId());
                    map.put("user",user);
                }else{

                    User user = userService.selectById(message.getFromId());
                    map.put("fromWho",user.getNickname());
                    map.put("user",user);
                }
                res.add(map);
            }
        }
        return res;

    }
    //得到信息列表
    public List<Map<String, Object>> getMessagesInfo(List<Message> messages){
        User hostUser = hostHolder.getUser();
        List<Map<String, Object>> messagesInfo = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        Collections.reverse(messages);   //时间往后的靠前
        if(messages!=null){
            for(Message message : messages){
                Map<String, Object> map = new HashMap<>();
                map.put("message",message);
                if(message.getFromId()!=hostUser.getId()){
                    //不把自己发送的信息设置为以读
                    ids.add(message.getId());
                }
                User user = hostUser;
                if(message.getFromId()!=hostUser.getId()) {
                    user = userService.selectById(message.getFromId());
                }
                map.put("user",user);
                messagesInfo.add(map);
            }
        }

        //更新全部已读
        updateMessageStatus(ids,MESSAGE_STATUS_READ);
        return messagesInfo;
    }
    public int updateMessageStatus(List<Integer> ids,int status){
        if(ids==null || ids.size() == 0) return 0;
        return messageMapper.updateMessageStatus(ids,MESSAGE_STATUS_READ);
    }
    public List<Map<String, Object>> getNoticesInfo(List<Message> notices){
        List<Map<String, Object>> res = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        Collections.reverse(notices);
        if(notices!=null){
            for(Message notice : notices){
                Map<String, Object> noticeVO = new HashMap<>();
                String content = notice.getContent();
                ids.add(notice.getId());
                Map<String, Object> data = JSONObject.parseObject(content);
                noticeVO.put("user", userService.selectById((Integer)data.get("userId")));
                int entityType =(Integer) data.get("entityType");
                String entity = getEntity(entityType);
                String topic = getTopic(notice.getConversationId());
                notice.setContent(topic+entity);
                noticeVO.put("postId", data.get("postId")==null?0:data.get("postId"));

                noticeVO.put("notice",notice);

                res.add(noticeVO);
            }
        }
        updateMessageStatus(ids,MESSAGE_STATUS_READ);
        return res;
    }
    public String getTopic(String topic){
        if(topic.equals(TOPIC_LIKE)) {
            return "点赞";
        }else if(topic.equals(TOPIC_FOLLOW)){
            return "关注";
        }
        return "回复";
    }
    public String getEntity(int entityType){
        String entity;
        if(entityType == ENTITY_TYPE_POST){
            entity = "了你的帖子";
        }else if(entityType == ENTITY_TYPE_REPLY){
            entity = "了你的回复";
        }else{
            entity = "了你";
        }
        return entity;
    }
    public int addMessage(Message message){
        //message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        return messageMapper.insertMessage(message);
    }


    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }
    public List<Message> findMessages(String conversationId, int offset, int limit){
        return messageMapper.selectAllMessages(conversationId,offset,limit);
    }
    public int findMessagesCount(String conversationId){
        return messageMapper.selectMessageCount(conversationId);
    }
    public int findMessagesUnreadCount(int userId,String conversationId){
        return messageMapper.selectMessageUnreadCount(userId,conversationId);
    }

    public List<Message> selectNotices(int userId, String topic, int offset, int limit){
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }
    public List<Message> selectAllNotices(int userId, int offset, int limit){
        return messageMapper.selectAllNotices(userId,offset,limit);
    }
    public int selectNoticeUnreadCount(int userId ){
        return messageMapper.selectNoticeUnreadCount(userId);
    }
    public int selectNoticeCount(int userId ){
        return messageMapper.selectNoticeCount(userId);
    }

}
