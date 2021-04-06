package com.ajwlforever.forum.service;

import com.ajwlforever.forum.dao.ReplyMapper;
import com.ajwlforever.forum.dao.UserMapper;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.Reply;
import com.ajwlforever.forum.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回复的服务类
 * author:ajwlforever
 */
@Service
public class ReplyService {

    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private PostService  postService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insert(Reply reply) {
        //是帖子回复才+1
        if(reply.getFatherId()==0){
            Post post = postService.selectByPostId(reply.getPostId());
            postService.updateReplyAmount(post.getId(),post.getReplyAmount()+1);
        }
        reply.setContent(sensitiveFilter.filter(reply.getContent()));
        return insertReply(reply);
    }


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
