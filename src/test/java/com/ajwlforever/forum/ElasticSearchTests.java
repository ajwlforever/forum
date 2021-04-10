package com.ajwlforever.forum;


import com.ajwlforever.forum.dao.elasticsearch.PostRepository;
import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.PostService;
import com.ajwlforever.forum.utils.ESUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class ElasticSearchTests {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ESUtils<Post>  esUtils;
    @Test
    public void insertTest() throws IOException {
        String index = "posts";
        //全部帖子
        List<Post> posts = postService.selectAllPost(0,0,-1);
        for(Post post:posts){
            post.setContent(Jsoup.parse(post.getContent()).text());
            post.setTitle(Jsoup.parse(post.getTitle()).text());
        }
        boolean isExists = esUtils.existsIndex(index);
        System.out.println(index+"是否存在: "+ isExists);
        if(!isExists){
            esUtils.createIndex(index);
        }
        esUtils.bulkAdd(index, posts);
        //批量插入帖子
        esUtils.search(index,"tags","日麻",0,10);
        esUtils.search(index,"title","全新",0,10);

    }
    @Test
    public void searchTest() throws IOException {
        String index = "posts";
        List<Map<String, Object>> res1 = esUtils.search(index,"title","日麻",0,10);
        List<Map<String, Object>> res2 = esUtils.search(index,"content","全新",0,10);
        System.out.println(res1);
        System.out.println(res2);
    }
    @Test
    public void deleteTest() throws IOException {
        String index  = "posts";
        System.out.println(esUtils.deleteIndex(index));
    }

    @Test
    public void mutilFieldSearchTest()throws IOException {
        String index = "posts";
        Map<String, String> data = new HashMap<>();
        data.put("title","全新");  //2篇帖子
        data.put("content","root"); //1篇帖子
        List<Map<String, Object>> res = esUtils.advancedSearch(index,data,0,10);
        System.out.println(res);
        System.out.println(res.size()); // 应该是3篇帖子
    }
}
