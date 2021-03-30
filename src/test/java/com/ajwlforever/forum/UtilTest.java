package com.ajwlforever.forum;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

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
}
