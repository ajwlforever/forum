package com.ajwlforever.forum.controller;


import com.ajwlforever.forum.annotation.LoginRequired;
import com.ajwlforever.forum.entity.Event;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.Reply;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.event.EventProducer;
import com.ajwlforever.forum.service.FollowService;
import com.ajwlforever.forum.service.LikeService;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.service.ReplyService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 对实体操作的控制器
 * author：ajwlforever
 * 实体分别为用户，帖子，回复，板块
 *
 * 对实体的操作包括：点赞，点踩，关注，分享，浏览（not all）
 * 都是双向操作，操作一方要记录操作，被操作一方要通知
 * - 点赞: like:entitytype:entityId -->  set(int)
 *
 *   - like:user:userId --> set(int)  收到赞的数量
 *
 * - 点踩: dislike:entitytype:entityId -->  set(int)
 *
 *   - dislike:user:userId --> set(int)  收到赞的数量
 *
 * - 关注:
 *
 *   -	关注者（某个实体粉丝）：follower:entityType:entityId
 *   -	关注了谁：followee:userId:entityType   -> zset(entityId,now) 按时间排序
 *
 * -	浏览：view:entityType:entityId : int
 *
 *
 */
@Controller
public class EntityOperationController implements ForumConstant {

    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private FollowService followService;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private PostService postService;
    @Autowired
    private ReplyService replyService;

    @LoginRequired
    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId,int postId ){
        // todo 是否要验证数据正确性?
        Map<String, Object> res = new HashMap<>();
        User hostUser = hostHolder.getUser();
        //点赞
        likeService.like(hostUser.getId(),entityType,entityId,entityUserId);
        //回显点赞数据
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        //是点赞还是取消赞，前端显示
        int likeStatus = likeService.findEntityLikeStatus(hostUser,entityType,entityId);

        res.put("likeCount", likeCount);
        res.put("likeStatus", likeStatus);

        //todo 发送点赞通知

        Event event = new Event()
                .setUserId(hostUser.getId())
                .setTopic(TOPIC_LIKE)
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityUserId)
                .setData("postId",postId);
        eventProducer.fireEvent(event );

        return ForumUtils.toJsonString(0,"点赞成功",res);
    }
    @PostMapping("/dislike")
    @LoginRequired
    @ResponseBody
    public String dislike(int entityType, int entityId, int entityUserId,int postId ){
        // todo 是否要验证数据正确性?
        Map<String, Object> res = new HashMap<>();
        User hostUser = hostHolder.getUser();
        //点踩
        likeService.dislike(hostUser.getId(),entityType,entityId,entityUserId);
        //回显点踩数据
        long dislikeCount = likeService.findEntityDisLikeCount(entityType,entityId);
        //是点踩还是取消踩，前端显示
        int dislikeStatus = likeService.findEntityDisLikeStatus(hostUser,entityType,entityId);

        res.put("dislikeCount", dislikeCount);
        res.put("dislikeStatus", dislikeStatus);

        return ForumUtils.toJsonString(0,"点踩成功",res);
    }
    //关注 entityUserId 实体的主人
    @PostMapping("/follow")
    @LoginRequired
    @ResponseBody
    public String follow(int entityType, int entityId){

        User hostUser = hostHolder.getUser();
        Map<String, Object> res = new HashMap<>();
        //关注
        //登录用户是否关注了这个实体
        boolean isFollowed = followService.isFollow(hostUser,entityType,entityId);
        if(!isFollowed){
            followService.follow(entityType,entityId,hostUser.getId());

            res.put("isFollowed",1);
            //有多少人关注了这个实体
            res.put("followCount",followService.findFansCount(entityType,entityId));
            //todo 发送关注通知
            Event event = new Event()
                    .setUserId(hostUser.getId())
                    .setTopic(TOPIC_FOLLOW)
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityId);
            //关注的是啥
            if(entityType == ENTITY_TYPE_POST){
                Post post = postService.selectByPostId(entityId);
                event.setEntityUserId(post.getUserId());
                event.setData("postId",entityId);
            }else{
                Reply reply = replyService.selectById(entityId);
                event.setEntityUserId(reply.getUserId());
                event.setData("postId",reply.getPostId());
            }
            eventProducer.fireEvent(event);

            return ForumUtils.toJsonString(0,"关注成功",res);
        }else{
            followService.unfollow(entityType,entityId,hostUser.getId());
            res.put("isFollowed",0);
            //有多少人关注了这个实体
            res.put("followCount",followService.findFansCount(entityType,entityId));

            return ForumUtils.toJsonString(0,"取关成功",res);
        }


    }
    //取关
    @PostMapping("/unfollow")
    @LoginRequired
    @ResponseBody
    public String unfollow(int entityType, int entityId){
        User hostUser = hostHolder.getUser();
        //关注
        followService.unfollow(entityType,entityId,hostUser.getId());
        return ForumUtils.toJsonString(0,"取关成功");
    }

}
