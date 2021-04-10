package com.ajwlforever.forum.dao.elasticsearch;

import com.ajwlforever.forum.entity.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ElasticsearchRepository<Post,Integer> {
}
