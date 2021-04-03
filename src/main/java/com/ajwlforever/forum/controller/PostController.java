package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.dao.BoardMapper;
import com.ajwlforever.forum.entity.*;
import com.ajwlforever.forum.event.EventProducer;
import com.ajwlforever.forum.service.*;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private EventProducer eventProducer;

    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable("postId")int postId, Page page, Model model){

        User hostUser = hostHolder.getUser();
        // 加载帖子
        Post post = postService.selectByPostId(postId);
        if(post == null ) return "error";
        User  postUser = userService.selectById(post.getUserId());


        model.addAttribute("user",postUser);
        model.addAttribute("post",post);
        //tags
        List<String> tagList = (List<String>) JSONObject.parse(post.getTags());
        model.addAttribute("tags",tagList);
        long AllLikeCount = 0;
        //帖子的赞踩处理
        //帖子的点赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
        AllLikeCount+=likeCount;
        int likeStatus = likeService.findEntityLikeStatus(hostUser,ENTITY_TYPE_POST,post.getId());

        model.addAttribute("likeCount",likeCount);
        model.addAttribute("likeStatus",likeStatus);
        //帖子的点踩
        long dislikeCount = likeService.findEntityDisLikeCount(ENTITY_TYPE_POST,post.getId());

        int dislikeStatus = likeService.findEntityDisLikeStatus(hostUser,ENTITY_TYPE_POST,post.getId());


        model.addAttribute("dislikeCount",dislikeCount);
        model.addAttribute("dislikeStatus",dislikeStatus);
        //帖子的关注
        long allFollowCount = 0;
        long followCount = followService.findFansCount(ENTITY_TYPE_POST,post.getId());
        boolean  isFollowed = followService.isFollow(hostUser,ENTITY_TYPE_POST,post.getId());


        model.addAttribute("followCount",followCount);
        model.addAttribute("isFollowed",isFollowed == true? 1:0);
        allFollowCount+=followCount;
        //Reply 处理
        int replyAmount = post.getReplyAmount();
        page.setLimit(PAGE_REPLY_LIMIT);
        page.setRows(replyAmount);
        page.setPath("/post/"+post.getId());
        if(replyAmount!=0){
        //没有回复
            List<Map<String,Object>> replies = new ArrayList<>();
            List<Reply> repliyList = replyService.selectByPostId(post.getId(), page.getOffset(), page.getLimit());
            for(Reply reply : repliyList)
            {
                Map<String, Object> replyMap = new HashMap<>();
                replyMap.put("reply",reply);  //回复
                User replyUser = userService.selectById(reply.getUserId());
                replyMap.put("user",replyUser);  //回复的主人
                //回复的点赞
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_REPLY,reply.getId());
                likeStatus = likeService.findEntityLikeStatus(hostUser,ENTITY_TYPE_REPLY,reply.getId());
                replyMap.put("likeCount",likeCount);
                replyMap.put("likeStatus",likeStatus);
                AllLikeCount+=likeCount;
                //回复的点踩
                dislikeCount = likeService.findEntityDisLikeCount(ENTITY_TYPE_REPLY,reply.getId());
                dislikeStatus = likeService.findEntityDisLikeStatus(hostUser,ENTITY_TYPE_REPLY,reply.getId());
                replyMap.put("dislikeCount",dislikeCount);
                replyMap.put("dislikeStatus",dislikeStatus);

                //回复的关注
                followCount = followService.findFansCount(ENTITY_TYPE_REPLY,reply.getId());
                isFollowed = followService.isFollow(hostUser,ENTITY_TYPE_REPLY,reply.getId());
                replyMap.put("followCount",followCount);
                replyMap.put("isFollowed",isFollowed);
                allFollowCount+=followCount;

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
        }
        model.addAttribute("allLikeCount",AllLikeCount);
        model.addAttribute("allFollowCount",allFollowCount);
        //浏览人数
        long allViewCount = viewService.getViewEntitycount(ENTITY_TYPE_POST,post.getId());
        model.addAttribute("allViewCount",allViewCount);
        //用户人数
        long allUserCount = viewService.getEntityUserCount(ENTITY_TYPE_POST,post.getId());
        model.addAttribute("allUserCount",allUserCount);
        //浏览人数加1
        viewService.viewEntity(hostUser ,ENTITY_TYPE_POST, post.getId(), USER_OPERATION_LOOK);

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

    @PostMapping("/post/reply/create")
    @ResponseBody
    public String createPostReply(Reply reply,Model model) {

        if(StringUtils.isBlank(reply.getContent())||reply.getContent().length() > 160 ||reply.getPostId()==0){
            return ForumUtils.toJsonString(1,"有空数据，回复失败!");
        }
        User user = hostHolder.getUser();
        reply.setUserId(user.getId()).setReplyTime(new Date()
        ).setStatus(REPLY_STATUS_NORMAL);
        replyService.insert(reply);
        //发送评论系统通知
        Event event = new Event()
                .setUserId(user.getId())
                .setTopic(TOPIC_REPLY)
                .setEntityType(reply.getFatherId()==0? ENTITY_TYPE_POST:ENTITY_TYPE_REPLY)  //对帖子回复还是回复 回复
                .setEntityId(reply.getId())
                .setData("postId",reply.getPostId());
        if(reply.getFatherId() == 0)
        {
            //是评论的回复
            Post post = postService.selectByPostId(reply.getPostId());
            event.setEntityUserId(post.getUserId());
        }else{
            Reply reply1 = replyService.selectById(reply.getFatherId());
            event.setEntityUserId(reply1.getUserId());
        }
        eventProducer.fireEvent(event);

        viewService.viewEntity(user,ENTITY_TYPE_POST,reply.getPostId(),USER_OPERATION_REPLY);
        return ForumUtils.toJsonString(0,"回复成功!");
    }
}
