package com.ajwlforever.forum;

import com.ajwlforever.forum.utils.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sf;
    @Test
    public void test1(){
        System.out.println(sf.filter("我日你妈"));
        System.out.println(sf.filter("你妈死了，你妈，死了狗崽种！我草"));
    }
}

