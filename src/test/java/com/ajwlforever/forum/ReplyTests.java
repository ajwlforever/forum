package com.ajwlforever.forum;


import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.Reply;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.service.ReplyService;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class ReplyTests implements ForumConstant {

    @Autowired
    private ReplyService replyService;
    @Autowired
    private PostService postService;

    @Test
    public void insertTest(){
        int postId = 5;
        Post post = postService.selectByPostId(postId);
        Reply r1 = new Reply().setPostId(postId).setUserId(2).setFatherId(0).setReplyTime(new Date())
                .setStatus(REPLY_STATUS_NORMAL).setContent("大佬好厉害，根本没想到");
        Reply r2 = new Reply().setPostId(postId).setUserId(2).setFatherId(0).setReplyTime(new Date())
                .setStatus(REPLY_STATUS_NORMAL).setContent("位运算的奥妙竟然如此之多");
        Reply r3 = new Reply().setPostId(postId).setUserId(4).setFatherId(0).setReplyTime(new Date())
                .setStatus(REPLY_STATUS_NORMAL).setContent("平生不会相思，才会相思，便害相思。____徐再思《折桂令·春情》");
        Reply r31 = new Reply().setPostId(postId).setUserId(2).setFatherId(4).setReplyTime(new Date())    //对r3的回复
                .setStatus(REPLY_STATUS_NORMAL).setContent("山无陵，江水为竭。冬雷震震，夏雨雪。天地合，乃敢与君绝。____佚名《上邪》");
        Reply r32 = new Reply().setPostId(postId).setUserId(2).setFatherId(4).setReplyTime(new Date())    //对r3的回复
                .setStatus(REPLY_STATUS_NORMAL).setContent("入我相思门，知我相思苦。____李白《三五七言 / 秋风词》");
        insert(r1,postId);
        insert(r2,postId);
        insert(r3,postId);
        insert(r31,postId);
        insert(r32,postId);
    }
    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void insert(Reply reply,int postId)
    {
        Post post = postService.selectByPostId(postId);
        replyService.insertReply(reply);
        //是帖子回复才+1
       if(reply.getFatherId()==0) postService.updateReplyAmount(post.getId(),post.getReplyAmount()+1);
    }

    @Test
    public void selectTests()
    {
        int postId = 5;
        int fatherId = 4;  //id为4的回复
        int userId  = 2;
        List<Reply> replies = replyService.selectByPostId(postId, 0 ,10);
        for (Reply reply :replies) System.out.println(reply);

        replies = replyService.selectByFatherId(fatherId,0 ,10);
        for (Reply reply :replies) System.out.println(reply);

        replies = replyService.selectByUserId(userId, 0,10);
        for (Reply reply :replies) System.out.println(reply);
    }
}
