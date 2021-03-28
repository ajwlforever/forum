package com.ajwlforever.forum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User的控制器
 * author:ajwlforever
 */

@Controller
@RequestMapping("/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.domain}")
    private String doMain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.path.upload}")
    private String uploadPath;

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
}
