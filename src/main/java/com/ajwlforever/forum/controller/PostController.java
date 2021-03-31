package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.dao.BoardMapper;
import com.ajwlforever.forum.entity.*;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.service.ReplyService;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostController implements ForumConstant {

    @Autowired
    private PostService postService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ReplyService replyService;
    @Value("${server.servlet.context-path}")
    private String CONTEXTPATH;

    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable("postId")int postId, Page page, Model model){
        // 加载帖子
        Post post = postService.selectByPostId(postId);
        if(post == null ) return "error404";
        User  postUser = userService.selectById(post.getUserId());


        model.addAttribute("user",postUser);
        model.addAttribute("post",post);
        //tags
        List<String> tagList = (List<String>) JSONObject.parse(post.getTags());
        model.addAttribute("tags",tagList);
        //Reply 处理
        page.setLimit(PAGE_REPLY_LIMIT);
        page.setRows(post.getReplyAmount());
        page.setPath("/post/"+post.getId());
        List<Map<String,Object>> replies = new ArrayList<>();
        List<Reply> repliyList = replyService.selectByPostId(post.getId(), page.getOffset(), page.getLimit());
        for(Reply reply : repliyList)
        {
            Map<String, Object> replyMap = new HashMap<>();
            replyMap.put("reply",reply);  //回复
            User replyUser = userService.selectById(reply.getUserId());
            replyMap.put("user",replyUser);  //回复的主人
            //回复的回复
           //todo 回复的回复暂时没有限制，全部找出来
            List<Reply> rereplyList = replyService.selectByFatherId(reply.getId(), 0, 0);
            if(rereplyList!=null || rereplyList.size()!=0) {
                List<Map<String, Object>> rereplies = new ArrayList<>();
                for (Reply rereply : rereplyList) {
                        //todo 回复的回复 回复的对象。
                    Map<String, Object> rereplyMap = new HashMap<>();
                    rereplyMap.put("reply", rereply);  //回复的回复
                    User rereplyUser = userService.selectById(rereply.getUserId());
                    rereplyMap.put("user", rereplyUser); //回复的回复的主人

                    rereplies.add(rereplyMap);
                }
                replyMap.put("rereplies", rereplies);
            }
            replies.add(replyMap);
        }
        model.addAttribute("replies",replies);
        return "/page-single-topic";
    }
    @GetMapping("/post/create")
    public String getCreatePostPage(Model model) {
         //板块注入
        List<Board> boards = boardMapper.selectAllBoards();
        model.addAttribute("boards",boards);
        return "page-create-topic";
    }

    @PostMapping("/post/create")
    @ResponseBody
    public String createPost(Post post, Model model){
        User user = hostHolder.getUser();
        String msg = "";
        int code = 0;
        if(post.getTitle().length()>50){
            code = 1;
            msg+="帖子标题过长";
            return ForumUtils.toJsonString(code,msg);
        }
        post.setUserId(user.getId());
         postService.createPost(post);

        return ForumUtils.toJsonString(code,msg) ;
    }
}
