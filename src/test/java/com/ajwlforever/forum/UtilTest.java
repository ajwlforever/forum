package com.ajwlforever.forum;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class UtilTest {

    @Test
    public void tagsTests() {
        List<String> tagList = new ArrayList<>();
        tagList.add("exchange");
        tagList.add("messages");
        System.out.println(tagList.get(1).getClass());
        String str = JSONObject.toJSONString(tagList);
        System.out.println(str);
        tagList = (List<String>)JSONObject.parse(str);
        System.out.println(tagList.get(1).getClass());

    }
    @Test
    public void testHtml(){

        String ss =   HtmlUtils.htmlUnescape("<span%20style=%27color:#EA2027'>日麻</span>");
        Document d = Jsoup.parse(ss);
        String text = d.text();
        System.out.println(text);


    }
}
