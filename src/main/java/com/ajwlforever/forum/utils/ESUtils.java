package com.ajwlforever.forum.utils;

import com.ajwlforever.forum.entity.Post;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.TimeUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class ESUtils<T> {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * 创建索引
     * @param index
     * @return
     * @throws IOException
     */
    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse response = client.indices().create(request,RequestOptions.DEFAULT);
        return  response.isAcknowledged();
    }

    /**
     * 删除索引
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = client.indices().delete(request,RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 判断某索引下文档id是否存在
     * @param index 索引
     * @param id 文档id
     * @return
     * @throws IOException
     */
    public  boolean docExists(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index,id);
        //只判断索引是否存在不需要获取_source
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean exists = client.exists(request,RequestOptions.DEFAULT);
        return  exists;
    }

    /**
     *  添加文档
     * @param index
     * @param id
     * @param t
     * @return
     * @throws IOException
     */
    public boolean addDoc(String index, String id,T t) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        //timeout
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        request.source(JSONObject.toJSONString(t), XContentType.JSON);
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        RestStatus Status = indexResponse.status();
        return Status==RestStatus.OK||Status== RestStatus.CREATED;
    }

    /**
     * 根据id来获取记录
     * @param index
     * @param id
     * @return
     * @throws IOException
     */
    public GetResponse getDoc(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index,id);
        GetResponse getResponse = client.get(request,RequestOptions.DEFAULT);
        return getResponse;
    }

    /**
     * 批量添加文档记录
     * @param index
     * @param list
     * @return
     * @throws IOException
     */
    public boolean bulkAdd(String index, List<T> list) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        // timeout
        bulkRequest.timeout(TimeValue.timeValueMinutes(2));
        bulkRequest.timeout("2m");
        for(T t :list){
            String post = JSONObject.toJSONString(t);
            IndexRequest request  = new IndexRequest(index).source(post, XContentType.JSON);
            if( t  instanceof Post){
                Post post1 = (Post) t;
                request.id(String.valueOf(post1.getId()));
            }
            bulkRequest.add(request);
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest,RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }

    /**
     * 更新文档记录
     * @param index
     * @param id
     * @param t
     * @return
     * @throws IOException
     */
    public boolean updateDoc(String index, String id,T t) throws IOException {
        UpdateRequest request = new UpdateRequest(index,id);
        request.doc(JSONObject.toJSONString(t));
        request.timeout(TimeValue.timeValueMinutes(2));
        request.timeout("2m");
        UpdateResponse response = client.update(request,RequestOptions.DEFAULT);
        return response.status()==RestStatus.OK;
    }

    /**
     * 删除文档记录
     * @param index
     * @param id
     * @return
     * @throws IOException
     */
    public boolean deleteDoc(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index,id);
        request.timeout(TimeValue.timeValueMinutes(2));
        request.timeout("2m");
        DeleteResponse response = client.delete(request,RequestOptions.DEFAULT);
        return response.status()==RestStatus.OK;

    }

    /**
     *  精确搜索
     * @param index 索引
     * @param field 字段
     * @param key 搜索内容
     * @param from 分页的开始
     * @param size 返回多少个数据
     * @throws IOException
     */
    public List<Map<String, Object>> search(String index, String field, String key, Integer from, Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //精确搜索这个key
        sourceBuilder.query(QueryBuilders.matchQuery(field, key));
        //分页控制
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        //最大搜索时间
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(field);
        highlightBuilder.requireFieldMatch(false); //多个字段高亮
        highlightBuilder.preTags("<span style='color:#EA2027'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        //开始搜索
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(response.getHits()));
        //解析完成后，高亮显示是另外放置，要解析到全文中
        return parseHits(response.getHits());

    }

    public List<Map<String, Object>> advancedSearch(String index, Map<String, String> data, Integer from, Integer size) throws IOException {


        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        // query构建 ,多字段
        for(String key : data.keySet()){
            queryBuilder.should().add(QueryBuilders.matchQuery(key,data.get(key)));
        }
        sourceBuilder.query(queryBuilder);

        //分页控制
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        //最大搜索时间
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for(String key : data.keySet()) {
            highlightBuilder.field(new HighlightBuilder.Field(key).fragmentSize(150).noMatchSize(150).numOfFragments(3));
        }
       // highlightBuilder.requireFieldMatch(false); //多个字段高亮
        highlightBuilder.preTags("<span style='color:#EA2027'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        //开始搜索

        searchRequest.source(sourceBuilder);
        System.out.println(searchRequest);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(response.getHits()));

        return parseHits(response.getHits());

    }


    public List<Map<String, Object>> parseHits(SearchHits searchHits){
        List<Map<String, Object>> res = new ArrayList<>();  //存放最后结果，映射的是实体类的
        //开始解析
        for(SearchHit hit : searchHits){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Set<String> keySet = highlightFields.keySet();

            Map<String,Object> source = hit.getSourceAsMap();  //获取source
            for( String field : keySet)
            {
                // 高亮替换
                HighlightField  highlightField = highlightFields.get(field);
                if(highlightField != null){
                    //命中，将source中的字段替换成高亮字段
                    Text[] fragments = highlightField.fragments();
                    String name = "";
                    for (Text text : fragments) {
                        name += text;
                }
                source.put(field, name);  //替换

                }
            }
                res.add(source);
        }
        return res;
    }

}
