package com.ajwlforever.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

@EnableElasticsearchRepositories
@SpringBootApplication
public class ForumApplication {

	@PostConstruct
	public void init(){
		// 解决netty启动冲突
		//@see Netty4Utils.setAvailableProcessors();
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
