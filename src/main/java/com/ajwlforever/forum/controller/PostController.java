package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.dao.BoardMapper;
import com.ajwlforever.forum.entity.Board;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @GetMapping("/post/{podtId}")
    public String getPost(@PathVariable("postId")int postId, Model model){
        // 加载帖子
        Post post = postService.selectByPostId(postId);
        if(post == null ) return "error404";
        User  postUser = userService.selectById(post.getUserId());

        model.addAttribute("user",postUser);
        model.addAttribute("post",post);

        return "/";
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
