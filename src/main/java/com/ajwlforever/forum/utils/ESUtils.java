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
     * ????????????????????????
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
     * ????????????
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
     * ????????????
     * @param index
     * @return
     */
    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = client.indices().delete(request,RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * ????????????????????????id????????????
     * @param index ??????
     * @param id ??????id
     * @return
     * @throws IOException
     */
    public  boolean docExists(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index,id);
        //??????????????????????????????????????????_source
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean exists = client.exists(request,RequestOptions.DEFAULT);
        return  exists;
    }

    /**
     *  ????????????
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
     * ??????id???????????????
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
     * ????????????????????????
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
     * ??????????????????
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
     * ??????????????????
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
     *  ????????????
     * @param index ??????
     * @param field ??????
     * @param key ????????????
     * @param from ???????????????
     * @param size ?????????????????????
     * @throws IOException
     */
    public List<Map<String, Object>> search(String index, String field, String key, Integer from, Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //??????????????????key
        sourceBuilder.query(QueryBuilders.matchQuery(field, key));
        //????????????
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        //??????????????????
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //????????????
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(field);
        highlightBuilder.requireFieldMatch(false); //??????????????????
        highlightBuilder.preTags("<span style='color:#EA2027'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        //????????????
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(response.getHits()));
        //?????????????????????????????????????????????????????????????????????
        return parseHits(response.getHits());

    }

    public List<Map<String, Object>> advancedSearch(String index, Map<String, String> data, Integer from, Integer size) throws IOException {


        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        // query?????? ,?????????
        for(String key : data.keySet()){
            queryBuilder.should().add(QueryBuilders.matchQuery(key,data.get(key)));
        }
        sourceBuilder.query(queryBuilder);

        //????????????
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        //??????????????????
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //????????????
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for(String key : data.keySet()) {
            highlightBuilder.field(new HighlightBuilder.Field(key).fragmentSize(150).noMatchSize(150).numOfFragments(3));
        }
       // highlightBuilder.requireFieldMatch(false); //??????????????????
        highlightBuilder.preTags("<span style='color:#EA2027'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        //????????????

        searchRequest.source(sourceBuilder);
        System.out.println(searchRequest);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(response.getHits()));

        return parseHits(response.getHits());

    }


    public List<Map<String, Object>> parseHits(SearchHits searchHits){
        List<Map<String, Object>> res = new ArrayList<>();  //?????????????????????????????????????????????
        //????????????
        for(SearchHit hit : searchHits){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Set<String> keySet = highlightFields.keySet();

            Map<String,Object> source = hit.getSourceAsMap();  //??????source
            for( String field : keySet)
            {
                // ????????????
                HighlightField  highlightField = highlightFields.get(field);
                if(highlightField != null){
                    //????????????source?????????????????????????????????
                    Text[] fragments = highlightField.fragments();
                    String name = "";
                    for (Text text : fragments) {
                        name += text;
                }
                source.put(field, name);  //??????

                }
            }
                res.add(source);
        }
        return res;
    }

}
