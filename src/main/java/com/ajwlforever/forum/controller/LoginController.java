package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录注册控制器
 * author:ajwlforever
 */

@Controller
public class LoginController implements ForumConstant {


    private  static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;


    @GetMapping("/login")
    public String login(Model model) {
        return "page-login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        return "page-signup";
    }

    @PostMapping("/login")
    @ResponseBody
    public String loginCheck(String username,String password,boolean rememberMe,
                             Model model, HttpServletResponse response) {
        User user = new User().setUsername(username).setPassword(password);
        int exprireTimes = !rememberMe ? DEFAULT_EXPIRED_TIME : REMEMBER_EXPIRED_TIME;
        Map<String, Object> res = userService.login(user,exprireTimes);
        if(!res.containsKey("ticket")) {
            //登录失败
            return ForumUtils.toJsonString(1,"用户名或密码错误") ;
        }else{
            //验证通过 登录成功
            Cookie cookie = new Cookie("ticket",(String)res.get("ticket"));
            cookie.setPath(contextPath);
            cookie.setMaxAge(exprireTimes);
            response.addCookie(cookie);

            return ForumUtils.toJsonString(0) ;
        }

    }
    @PostMapping("/register")
    @ResponseBody
    public String registerCheck(User user, Model model) {
        System.out.println(user);
        Map<String, Object> res = new HashMap<>();
        res = userService.resgiter(user);
        if(res==null || res.isEmpty()) {
            return ForumUtils.toJsonString(0,"注册成功，我们已经望您的邮箱发送了一封激活邮件，请尽快激活！");
        }else {
            return ForumUtils.toJsonString(1,"注册失败！",res);
        }
    }
    @GetMapping("/activation/{userId}/{code}")
    public String activation(@PathVariable("userId")int userId,@PathVariable("code")String code, Model model)
    {
            int res = userService.activation(userId,code);
            if(res == activation_success ) {
                 model.addAttribute("msg","激活成功,您的账号已经可以正常使用");
                 model.addAttribute("target","/login");
            }else {
                if(res == activation_repeat) {
                    model.addAttribute("msg", "重复激活,您的账号已经激活过");
                    model.addAttribute("target","/login");
                }
                else {
                    model.addAttribute("msg", "激活失败，账号不存在或激活码不正确！");
                    model.addAttribute("target","/register");
                }
            }
            return "operate-result";
    }

     //验证码
     @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        // todo  验证码放入redis
        Cookie cookie = new Cookie("kaptch",text);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        //图片写入response
        response.setContentType("image/png");
        try {
            OutputStream os  = response.getOutputStream();
            ImageIO.write(image,"png",os);

        }catch (IOException e)
        {
            logger.error("响应验证码失败"+e.getMessage());
            e.printStackTrace();
        }

    }
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket")String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
