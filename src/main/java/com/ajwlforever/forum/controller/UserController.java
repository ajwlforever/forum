package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.entity.Page;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.Reply;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.service.ReplyService;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * User的控制器
 * author:ajwlforever
 */

@Controller
@RequestMapping("/user")
public class UserController implements ForumConstant {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.domain}")
    private String doMain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ReplyService replyService;

    @GetMapping("/img/{filename}")
    public void getImage(@PathVariable("filename")String fileName, HttpServletResponse response){
        fileName = uploadPath+"/"+fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        suffix = suffix.substring(1);
        response.setContentType("image/"+suffix);
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int b=0;
            while(  (b=fileInputStream.read(buffer))!= -1)
            {
                os.write(buffer,0,b);
            }

        } catch (FileNotFoundException e) {
            logger.error("读取图片失败");

        } catch (IOException e) {
            logger.error("读取图片失败");

        }
    }

    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable("userId") int userId, Page page,Model model) {
        User user = hostHolder.getUser();
        User profileUser = user;
        if(userId!=user.getId()) {
            profileUser = userService.selectById(userId);
        }
        //todo 个人中心具体化
        page.setPath("/profile/"+profileUser.getId());
        page.setLimit(PAGE_PROFILE_LIMIT);
        page.setRows(postService.selectPostRows(profileUser.getId()));
        model.addAttribute("user",profileUser);
        List<Post> posts = postService.selectAllPost(profileUser.getId(), page.getOffset(), page.getLimit());

        model.addAttribute("posts", posts);
        model.addAttribute("colorLevel", new Random().nextInt(10)+10);

        return "page-single-user";
    }

    @PostMapping("/upload")
    public String uploadHead(MultipartFile headerImage, Model model)
    {
        //todo 图片功能升级，修改大小，压缩，防止过多上传。
        if(headerImage==null)
        {
            model.addAttribute("error","您还没选择文件");
            return "error";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(suffix.equals(".png")||suffix.equals(".jpg")||suffix.equals(".jpeg")||suffix.equals(".bmp")||
                suffix.equals(".gif"))
        { //文件类型是图片
        }
        else
        {
            //不是图片返回错误
            model.addAttribute("error","文件格式不正确");
            return "/error";
        }
        if(StringUtils.isBlank(suffix))
        {
            model.addAttribute("error","文件格式不正确");
            return "/error";
        }
        // confirm Filename
        fileName = ForumUtils.generateUUID()+suffix;
        File dest = new File(uploadPath+"/"+fileName);
        try {
            //文件转移
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw  new RuntimeException("上传文件失败，服务器发生异常",e);

        }
        //update Url
        User user = hostHolder.getUser();
        String url = doMain+contextPath+"/user/img/"+fileName;
        userService.updateHeader(user.getId(),url);


        return "redirect:/index";
    }

    @PostMapping("/setting")
    @ResponseBody
    public String changeUserInfo(String nickName,String email,String password,String location, String website,
                                 String about){
        User hostUser = hostHolder.getUser();
        password = ForumUtils.md5(password+hostUser.getSalt());
        Map<String, Object> res = userService.changeUserInfo(hostUser.getId(),nickName,email,password,location,website,about);
        if(res.isEmpty()){
            //todo 修改数据验证 前端+后端
            return ForumUtils.toJsonString(1,"未有要修改的数据");
        }else {
            return ForumUtils.toJsonString(0,"个人设置修改成功！",res);
        }
    }
}
