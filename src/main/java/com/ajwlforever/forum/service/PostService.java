package com.ajwlforever.forum.service;


import com.ajwlforever.forum.dao.PostMapper;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import com.ajwlforever.forum.utils.SensitiveFilter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 帖子的服务层
 */
@Service
public class PostService implements ForumConstant {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private  ViewService viewService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Map<String, Object>> getPostMessages(List<Post> postList){
        List<Map<String,Object>> res = new ArrayList<>();
        User hostUser = hostHolder.getUser();
        if(postList!=null){
            for(Post post : postList)
            {

                Map<String, Object> postMessage = new HashMap<>();
                //帖子信息
                postMessage.put("post",post);
                postMessage.put("user",userService.selectById(post.getUserId()));
                //tags
                List<String> tagList = (List<String>) JSONObject.parse(post.getTags());
                postMessage.put("tags",tagList);
                //关注人数
                long followCount = followService.findFansCount(ENTITY_TYPE_POST,post.getId());
                boolean isFollowed  = followService.isFollow(hostUser,ENTITY_TYPE_POST,post.getId());

                //浏览人数
                long allViewCount = viewService.getViewEntitycount(ENTITY_TYPE_POST,post.getId());
                postMessage.put("allViewCount",allViewCount);
                postMessage.put("followCount",followCount);
                postMessage.put("isFollowed",isFollowed);
                postMessage.put("colorNumber",new Random().nextInt(10)+10);
                //最后活跃时间
                postMessage.put("lastActiveTime", ForumUtils.getLastActiveTime(post.getCreateTime()));
                //封装入PostMessages
                 res.add(postMessage);
            }
        }
        return res;
    }
    //创建帖子
    public int createPost(Post post)
    {
        post.setLevel(POST_LEVEL_NORMAL)
                .setCreateTime(new Date())
                .setReplyAmount(0)
                .setStatus(POST_STATUS_NOMAL)
                .setTags(ForumUtils.getTags(post.getTags()));
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

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
