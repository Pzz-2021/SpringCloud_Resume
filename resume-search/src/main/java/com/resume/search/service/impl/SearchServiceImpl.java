package com.resume.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.Position;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.resume.search.utils.ElasticSearchConstants.POSITION_INDEX;

@DubboService
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public Boolean savePosition(Position... positions) {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        ArrayList<Position> positionArr = new ArrayList<Position>(Arrays.asList(positions));

        // 批量请求处理
        for (Position position : positionArr) {
            bulkRequest.add(
                    // 这里是数据信息
                    new IndexRequest(POSITION_INDEX)
                            .id("" + position.getPkPositionId()) // 没有设置id 会自定生成一个随机id
                            .source(JSON.toJSONString(position), XContentType.JSON)
            );
        }

        BulkResponse response = null;
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//            System.out.println("批量添加结果：" + response.status());// OK
            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public Boolean deletePositionById(Long id) {
        DeleteRequest request = new DeleteRequest(POSITION_INDEX, "" + id);
        request.timeout("1s");

        DeleteResponse response = null;
        try {
            response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
//            System.out.println("删除状态：" + response.status());// OK
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updatePositionById(Position position) {
        UpdateRequest updateRequest = new UpdateRequest(POSITION_INDEX, "" + position.getPkPositionId());

        updateRequest.doc(JSON.toJSONString(position), XContentType.JSON);

        try {
            UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//            System.out.println("更新状态：" + response.status());// OK
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Position getPositionById(Long id) {
        GetRequest request = new GetRequest(POSITION_INDEX, "" + id);

        try {
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);

            String sourceAsString = response.getSourceAsString();
            return JSON.parseObject(sourceAsString, Position.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



}
