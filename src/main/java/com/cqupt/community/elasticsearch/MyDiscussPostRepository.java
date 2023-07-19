package com.cqupt.community.elasticsearch;


import com.cqupt.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
