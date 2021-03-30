package com.ajwlforever.forum.service;


import com.ajwlforever.forum.dao.PostMapper;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 帖子的服务层
 */
@Service
public class PostService implements ForumConstant {

    @Autowired
    private PostMapper postMapper;

    //创建帖子
    public int createPost(Post post)
    {
        post.setLevel(POST_LEVEL_NORMAL)
                .setCreateTime(new Date())
                .setReplyAmount(0)
                .setStatus(POST_STATUS_NOMAL)
                .setTags(ForumUtils.getTags(post.getTags()));
        return insertPost(post);
    }
    // 找所有帖子，userId为0是所有，>0是具体用户的帖子
    public List<Post> selectAllPost(int userId, int offset, int limit){
        return postMapper.selectAllPost(userId,offset,limit);
    }
    // 统计所有帖子的数量，userId为0是所有，>0是具体用户的帖子
    public int selectPostRows(int userId){
        return postMapper.selectPostRows(userId);
    }

    public int insertPost(Post post){
        return postMapper.insertPost(post);
    }

    public Post selectByPostId(int postId){
        return postMapper.selectByPostId(postId);
    }

    //更新 帖子状态，回复数量，最后活跃时间
    public int updateStatus(int postId,int status){
        return postMapper.updateStatus(postId,status);
    }
    public int updateReplyAmount(int postId,int amount){
        return postMapper.updateReplyAmount(postId,amount);
    }
    public int updateLastReplyTime(int postId, Date lastReplyTime){
        return postMapper.updateLastReplyTime(postId, lastReplyTime);
    }

}
