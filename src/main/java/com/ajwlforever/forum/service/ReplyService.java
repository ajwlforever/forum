package com.ajwlforever.forum.service;

import com.ajwlforever.forum.dao.ReplyMapper;
import com.ajwlforever.forum.dao.UserMapper;
import com.ajwlforever.forum.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回复的服务类
 * author:ajwlforever
 */
@Service
public class ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    //查找回复
    public Reply selectById(int id){
        return replyMapper.selectById(id);
    }
    public List<Reply> selectByPostId(int postId, int offset, int limit){
        return replyMapper.selectByPostId(postId, offset, limit);
    }
    public List<Reply> selectByUserId(int userId, int offset, int limit){
        return replyMapper.selectByUserId(userId, offset, limit);
    }

    public List<Reply> selectByFatherId(int fatherId, int offset, int limit){
        return replyMapper.selectByFatherId(fatherId, offset, limit);
    }

    //插入回复
    public int insertReply(Reply reply){
        return replyMapper.insertReply(reply);
    }

    //修改回复
    public int updateReplyStatus(int id, int status){
        return replyMapper.updateReplyStatus(id,status);
    }
}
