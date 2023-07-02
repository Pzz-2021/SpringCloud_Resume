package com.resume.search.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.resume.base.model.TokenInfo;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDto;
import com.resume.dubbo.domian.SearchCondition;
import com.resume.search.mapstruct.PositionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.resume.base.utils.Constant.*;
import static com.resume.dubbo.domian.PositionConstant.*;
import static com.resume.search.utils.ElasticSearchConstants.POSITION_INDEX;

@DubboService
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public Boolean savePositionDto(PositionDto... positions) {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        ArrayList<PositionDto> positionArr = new ArrayList<PositionDto>(Arrays.asList(positions));

        // 批量请求处理
        for (PositionDto position : positionArr) {
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
    public Boolean deletePositionDtoById(Long id) {
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
    public Boolean updatePositionDtoById(PositionDto position) {
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
            PositionDto positionDto = JSON.parseObject(sourceAsString, PositionDto.class);

            // 将 Dto 转换成 pojo
            return PositionMapper.INSTANCE.convertToPosition(positionDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    // 查询
// SearchRequest 搜索请求
// SearchSourceBuilder 条件构造
// HighlightBuilder 高亮
// TermQueryBuilder 精确查询
// MatchAllQueryBuilder
// xxxQueryBuilder ...
    @Override
    public List<Position> searchPosition(SearchCondition searchCondition, TokenInfo tokenInfo) {
        // 1.创建查询请求对象
        SearchRequest searchRequest = new SearchRequest(POSITION_INDEX);
        // 2.构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // (1)查询条件 使用QueryBuilders工具类创建
        // 多条件查询
        // 搜索限定 公司、设置关键词
        BoolQueryBuilder termQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(COMPANY_ID, tokenInfo.getCompanyId()))
                .should(QueryBuilders.matchQuery(DESCRIPTION, searchCondition.getQuery()))
                .should(QueryBuilders.matchQuery(POSITION_NAME, searchCondition.getQuery()));

        switch (tokenInfo.getRole()) {
            case HR:
                termQueryBuilder.must(QueryBuilders.termQuery(HR_ID_LIST, tokenInfo.getPkUserId()));
                break;

            case INTERVIEWER:
                termQueryBuilder.must(QueryBuilders.termQuery(INTERVIEWER_ID_LIST, tokenInfo.getPkUserId()));
                break;

            case COMPANY_ADMIN:
                break;
        }

        //        // 匹配查询
        //        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        // (2)其他<可有可无>：（可以参考 SearchSourceBuilder 的字段部分）
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(POSITION_NAME);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        // 分页
        searchSourceBuilder.from((searchCondition.getPage() - 1) * searchCondition.getPageSize());
        searchSourceBuilder.size(searchCondition.getPageSize());

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // (3)条件投入
        searchSourceBuilder.query(termQueryBuilder);

        // 3.添加条件到请求
        searchRequest.source(searchSourceBuilder);
        // 4.客户端查询请求
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 5.查看返回结果
        SearchHits hits = search.getHits();
        System.out.println(JSON.toJSONString(hits));
        System.out.println("=======================");
//        List<Map<String, Object>> results = new ArrayList<>();
        List<Position> resultArr = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {
            // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField name = highlightFields.get(POSITION_NAME);

            // 替换
            if (name != null) {
                Text[] fragments = name.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                sourceAsMap.put(POSITION_NAME, new_name.toString());
            }
//            results.add(sourceAsMap);

            Position position = BeanUtil.fillBeanWithMap(sourceAsMap, new Position(), false);
            resultArr.add(position);
        }
        System.out.println(resultArr);

        return resultArr;
    }
}
