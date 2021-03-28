package com.ajwlforever.forum;


import com.ajwlforever.forum.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Test
    public void sendMail()
    {
        String to = "2353350597@qq.com";
        String subject = "春秋往事不能已！";
        String content = "<!--suppress ALL-->\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>激活邮件</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            box-sizing: border-box;\n" +
                "            font-family: Arial, Helvetica, sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            background-color: #ECECEC;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            width: 800px;\n" +
                "            margin: 50px auto;\n" +
                "        }\n" +
                "\n" +
                "        .header {\n" +
                "            height: 80px;\n" +
                "            background-color: #49bcff;\n" +
                "            border-top-left-radius: 5px;\n" +
                "            border-top-right-radius: 5px;\n" +
                "            padding-left: 30px;\n" +
                "        }\n" +
                "\n" +
                "        .header h2 {\n" +
                "            padding-top: 25px;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        .content {\n" +
                "            background-color: #fff;\n" +
                "            padding-left: 30px;\n" +
                "            padding-bottom: 30px;\n" +
                "            border-bottom: 1px solid #ccc;\n" +
                "        }\n" +
                "\n" +
                "        .content h2 {\n" +
                "            padding-top: 20px;\n" +
                "            padding-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .content p {\n" +
                "            padding-top: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            background-color: #fff;\n" +
                "            border-bottom-left-radius: 5px;\n" +
                "            border-bottom-right-radius: 5px;\n" +
                "            padding: 35px;\n" +
                "        }\n" +
                "\n" +
                "        .footer p {\n" +
                "            color: #747474;\n" +
                "            padding-top: 10px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"header\">\n" +
                "        <h2>欢迎加入AXForum </h2>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "        <h2>亲爱的用户您好</h2>\n" +
                "        <p>您的邮箱：<b><span th:text=\"${email}\"></span></b></p>\n" +
                "        <p>您的激活链接：<b><a href=\"\"></a></b></p>\n" +
                "        <p>您注册时的日期：<b><span th:text=\"${createTime}\"></span></b></p>\n" +
                "        <p>当您在使用本网站时，务必要遵守法律法规</p>\n" +
                "        <p>如果您有什么疑问可以联系管理员，Email: <b>wgt161110527@163.com</b></p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        <p>此为系统邮件，请勿回复</p>\n" +
                "        <p>请保管好您的信息，避免被他人盗用</p>\n" +
                "        <p>©shw</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
        mailClient.sendMail(to,subject,content);
    }
}
