package com.ajwlforever.forum.dao;

import com.ajwlforever.forum.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * reply的实体类
 * id int(11) AI PK
 * post_id int(11)
 * user_id int(11)
 * father_id int(11)
 * reply_time timestamp
 * content text
 * status int(1) UN zerofill
 */
@Mapper
@Repository
public interface ReplyMapper {

    //查找回复
    Reply selectById(int id);
    List<Reply> selectByPostId(int postId, int offset, int limit);
    List<Reply> selectByUserId(int userId,int offset, int limit);

    List<Reply> selectByFatherId(int fatherId, int offset, int limit);
    //统计数量
    //用户回复某个帖子的数量
    //插入回复
    int insertReply(Reply reply);

    //修改回复
    int updateReplyStatus(int id, int status);
}
