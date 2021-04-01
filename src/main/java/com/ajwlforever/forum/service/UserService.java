package com.ajwlforever.forum.service;

import com.ajwlforever.forum.dao.UserMapper;
import com.ajwlforever.forum.entity.LoginTicket;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.ForumUtils;
import com.ajwlforever.forum.utils.MailClient;
import com.ajwlforever.forum.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * user 的 Service
 * author：ajwlforever
 */


@Service
public class UserService implements ForumConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String doMain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailClient mailClient;


    public Map<String, Object> changeUserInfo(int userId,String nickName,String email,String password,String location, String website,
                                              String about){
        Map<String, Object> res = new HashMap<>();
        if(!StringUtils.isBlank(nickName)){
            userMapper.updateNickName(userId,nickName);
            res.put("nickNameMsg","昵称修改为"+nickName);
        }
        if(!StringUtils.isBlank(email))
        {
            //todo 重新激活email
            res.put("emailMsg", "邮件修改功能未完成");

        }
        if(!StringUtils.isBlank(password))
        {
            userMapper.updatePassword(userId,password);
            res.put("passwordMsg","密码已修改");
        }
        if(!StringUtils.isBlank(location)||!StringUtils.isBlank(website)||!StringUtils.isBlank(about))
        {
            String jsonStr = ForumUtils.getUserInfoJsonString(location,website,about);
            userMapper.updateInfo(userId,jsonStr);
            res.put("infoMsg","地址，个人网站，个人简介修改成功!");
        }
        return res;
    }
    //注册
    public Map<String, Object> login(User user, int expiredSeconds){
        Map<String, Object> res = new HashMap<>();
        if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())) {
            res.put("loginError","用户名或密码不能为空！");
            return res;
        }
        //用户是否存在
        User tmp = selectByName(user.getUsername());
        if(tmp==null) {
            res.put("loginError","用户名或密码错误！");
            return res;
        }
        String pwd = ForumUtils.md5(user.getPassword()+tmp.getSalt());
        if(!pwd.equals(tmp.getPassword())) {
            res.put("loginError","用户名或密码错误！");
            return res;
        }
        if(tmp.getStatus()==0){
            res.put("loginError","该用户未激活！");
            return res;
        }
        //登录成功,生成登录凭证传入redis
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(tmp.getId())
        .setStatus(0)
        .setTicket(ForumUtils.generateUUID())
        .setExpired(new Date(System.currentTimeMillis()+expiredSeconds * 1000));

        //存放在redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey,loginTicket);

        res.put("ticket",loginTicket.getTicket());
        return res;
    }
    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
       return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }
    public void logout(String ticket)
    {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket =(LoginTicket) redisTemplate.opsForValue().get(redisKey);

        loginTicket.setStatus(1); //shixiao
        redisTemplate.opsForValue().set(redisKey,loginTicket);
    }
    public Map<String, Object> resgiter(User user)
    {
        Map<String, Object> res = new HashMap<>();
//        if(!ForumUtils.isEmail(user.getEmail()))
//        {  //验证邮箱格式，前端验证
//            res.put("emailMsg","邮件格式错误");
//            return res;
//        }
        if(StringUtils.isBlank(user.getUsername()))
        {
            res.put("usernameMsg","用户名不能为空");
            return res;
        }
        if(StringUtils.isBlank(user.getEmail()))
        {
            res.put("emailMsg","邮箱不能为空");
            return res;
        }
        User tmp = selectByName(user.getUsername());
        if(tmp != null)
        {
            res.put("usernameMsg","用户已存在");
            return res;
        }
        tmp = selectByEmail(user.getEmail());
        if(tmp != null )
        {
            res.put("emailMsg","该邮箱已注册过!");
            return res;
        }
        //邮箱，用户名，密码都ok
        //开始往数据库插入User
        user.setNickname(ForumUtils.getRandomNickName())
        .setCreateTime(new Date())
        .setActiveTime(new Date())
        .setStatus(0)
        .setType(1)
        .setHeaderUrl(doMain+contextPath+"/user/img/"+new Random().nextInt(5)+".png")
        .setActiveCode(ForumUtils.generateUUID())
        .setSalt(ForumUtils.generateUUID().substring(1,5))
        .setPassword(ForumUtils.md5(user.getPassword()+user.getSalt()));
        System.out.println(user);
        insertUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url= doMain+contextPath+"/activation/"+user.getId()+"/"+user.getActiveCode();

        context.setVariable("url",url);
        context.setVariable("createTime",user.getCreateTime());
        context.setVariable("username",user.getUsername());

        String content = templateEngine.process("/registerTemplate",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);


        return res;
        //
        }


    public int activation(int userId,String code)
    {
        User user = selectById(userId);
        //无此用户或激活码错误。
        if(user==null || !user.getActiveCode().equals(code)) return activation_failed;
        else{

            if(user.getStatus()==1) return activation_repeat;
            else{
                updateStatus(userId,1);
                return activation_success;
            }
        }
    }
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }


    public User selectById(int userId){
        return userMapper.selectById(userId);
    }

    public User selectByName(String username){
        return userMapper.selectByName(username);
    }

    public User selectByEmail(String email){
        return userMapper.selectByEmail(email);
    }

    public int updateStatus(int userId, int status){
        return userMapper.updateStatus(userId, status);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId, headerUrl);
    }

    public int updatePassword(int userId, String password){
        return userMapper.updatePassword(userId, password);
    }


}
