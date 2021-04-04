package com.ajwlforever.forum.dao;

import com.ajwlforever.forum.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息的Mapper
 * author: ajwlforever;
 * id int(11) AI PK
 * from_id int(11)
 * to_id int(11)
 * conversation_id varchar(45)
 * content text
 * status int(11)
 * create_time timestamp
 */
@Mapper
@Repository
public interface MessageMapper {

    //所有与userId有关的消息
    // 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
    List<Message> selectConversations(int userId, int offset, int limit);
    List<Message> selectAllMessages(String conversationId, int offset, int limit);

    //查询用户会话的数量
    int selectConversationCount(int userId);
    // 会话中消息的数量
    int selectMessageCount(String conversationId);
    //某个会话中未读 用户所有的未读
    int selectMessageUnreadCount(int userId, String conversationId);

    //插入消息
    int insertMessage(Message message);

    //更新消息的状态
    // 0-未读;1-已读;2-删除;
    int updateMessageStatus(List<Integer> ids, int status);

    //系统消息

    List<Message> selectNotices(int userId, String topic, int offset, int limit);
    List<Message> selectAllNotices(int userId, int offset, int limit);
    int selectNoticeUnreadCount(int userId );
    int selectNoticeCount(int userId );

}
