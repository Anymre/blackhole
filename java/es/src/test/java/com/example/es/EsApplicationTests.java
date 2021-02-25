package com.example.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@SpringBootTest
class EsApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static String index = "i";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    public void create() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("address", "四川省成都市犀浦镇百草路" + 12 + i + "号");
            map.put("gender", "男");
            map.put("user_name", new Random(System.currentTimeMillis()).nextInt());
            map.put("post_date", LocalDateTime.now());
            map.put("age", 23 + i);
            map.put("height", 155 + i);
            list.add(map);
        }

        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> item : list) {
            bulkRequest.add(new IndexRequest(index).
                    source(item));
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(objectMapper.writeValueAsString(bulk));
    }

    @Test
    public void findOne() throws IOException {
        GetRequest getRequest = new GetRequest(index, "H_dP03cBjS8pOZwHZ26U");

        GetResponse search = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> hits = search.getSource();
        if (hits != null) {
            System.out.println(hits);
        }
    }

    @Test
    public void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder queryBuilder = new MatchAllQueryBuilder();
        sourceBuilder.query(queryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        if (hits != null) {
            System.out.println(Arrays.toString(hits.getHits()));
        }
    }

    @Test
    public void agg() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder queryBuilder = new MatchAllQueryBuilder();

        AvgAggregationBuilder age = AggregationBuilders.avg("age").field("age");
        sourceBuilder.query(queryBuilder);
        sourceBuilder.aggregation(age);
        sourceBuilder.size(0);

        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(objectMapper.writeValueAsString(search));
    }

}
