package com.example.es;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class EsApplicationTests {
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    @Test
    void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("i");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }
    
    @Test
    void create() throws IOException {
        SearchRequest searchRequest = new SearchRequest("i");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchAllQueryBuilder = new MatchQueryBuilder("a","b");
        sourceBuilder.query(matchAllQueryBuilder);
        
        searchRequest.source(sourceBuilder);
        searchRequest.setCcsMinimizeRoundtrips(false);
        searchRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN_CLOSED);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(search);
    }
    
}
