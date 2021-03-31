package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.entity.Page;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页控制器
 * author：ajwlforever
 *
 */
@Controller
public class HomeController implements ForumConstant {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @GetMapping({"/","/index"})
    public String getHome(Model model, Page page)
    {
        //封装帖子
        User hostUser = hostHolder.getUser();
        //设置总页数
        page.setRows(postService.selectPostRows(0));
        page.setPath("/index");
        System.out.println(page.getOffset()+":"+page.getLimit()+":"+page.getTotal());
        //获取帖子列表
        List<Post> postList = postService.selectAllPost(0,page.getOffset(),page.getLimit());

        List<Map<String,Object>> normalPostMessages = new ArrayList<>();
        List<Map<String,Object>> topPostMessages = new ArrayList<>();
        if(postList!=null){
            for(Post post : postList)
            {

                Map<String, Object> postMessage = new HashMap<>();
                //帖子信息
                postMessage.put("post",post);
                postMessage.put("user",userService.selectById(post.getUserId()));
                //tags
                List<String> tagList = (List<String>)JSONObject.parse(post.getTags());
                postMessage.put("tags",tagList);
                //最后活跃时间
                postMessage.put("lastActiveTime", ForumUtils.getLastActiveTime(post.getCreateTime()));
                //封装入PostMessages
                if(post.getStatus() == POST_STATUS_NOMAL){
                    normalPostMessages.add(postMessage);
                }else{
                    topPostMessages.add(postMessage);
                }


            }
        }
        model.addAttribute("normalPostMessages",normalPostMessages);
        model.addAttribute("topPostMessages",topPostMessages);
        return "index";
    }

    @RequestMapping(path = "/error" , method = RequestMethod.GET)
    public String getErrorPage()
    {
        return "/error404";
    }

}
