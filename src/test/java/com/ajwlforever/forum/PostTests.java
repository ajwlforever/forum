package com.ajwlforever.forum;


import com.ajwlforever.forum.dao.PostMapper;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 帖子数据库操作全部ok
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class PostTests implements ForumConstant {

    @Autowired
    private PostService postService;
    @Test
    public void  insertTest()
    {
        Post p1 = new Post()
                .setUserId(2)
                .setBoardName("管理之家")
                .setTitle("第一篇文章")
                .setContent("AXForum正式开启！")
                .setType(POST_TYPE_DISCUSS)
                .setStatus(POST_STATUS_NOMAL)
                .setLevel(POST_LEVEL_NORMAL)
                .setTags("{tags:{\"exchange\"}}")
                .setCreateTime(new Date())
                .setReplyAmount(0);
        System.out.println(p1);
        Post p2 = new Post()
                .setUserId(2)
                .setBoardName("管理之家")
                .setTitle("第二篇文章")
                .setContent("盐化秸秆加精料日粮对绵羊生产性能和消化的影响")
                .setType(POST_TYPE_DISCUSS)
                .setStatus(POST_STATUS_NOMAL)
                .setLevel(POST_LEVEL_NORMAL)
                .setTags("{tags:{\"exchange\"}}")
                .setCreateTime(new Date())
                .setReplyAmount(0);
        System.out.println(p2);
        postService.insertPost(p1);
        postService.insertPost(p2);
    }

    @Test
    public void selectTest() {
        System.out.println(postService);
        Post p1 = postService.selectByPostId(1);

        System.out.println(p1);
        int row1 = postService.selectPostRows(0);
        int row2 = postService.selectPostRows(2);
        System.out.println(row1+":"+row2);

    }
    @Test
    public void updateTest() {
        Post p1 = postService.selectByPostId(1);
        System.out.println(p1);
        postService.updateStatus(p1.getId(), 0); //删除这个帖子
        postService.updateReplyAmount(p1.getId(), 1234); //浏览人数1234
        postService.updateLastReplyTime(p1.getId(), new Date());
        p1 = postService.selectByPostId(1);
        System.out.println(p1);
    }

}
