package com.resume.position.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.SearchCondition;
import com.resume.position.mapper.PositionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.position.utils.CacheClient;
import lombok.Getter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 彭政
 * @since 2023-06-18
 */
@Getter
@Service
public class PositionService extends ServiceImpl<PositionMapper, Position> {

    @Autowired
    private CacheClient cacheClient;

    @DubboReference
    private SearchService searchService;

    @Resource
    private PositionMapper positionMapper;


    public PageBean<Position> selectPositionByPage(TokenInfo tokenInfo, int nowPage) {
        PageBean<Position> pageBean = new PageBean<>();
        Long userId = tokenInfo.getPkUserId();
        Long companyId = tokenInfo.getCompanyId();
        int offset = (nowPage - 1) * Constant.PAGE_SIZE;
        switch (tokenInfo.getRole()) {
            case Constant.COMPANY_ADMIN:
                pageBean.setData(positionMapper.selectPositionByAdmin(companyId, offset, Constant.PAGE_SIZE));
                pageBean.setTotalCount(positionMapper.totalCountPositionByAdmin(companyId));
                break;
            case Constant.HR:
                pageBean.setData(positionMapper.selectPositionByHr(userId, offset, Constant.PAGE_SIZE));
                pageBean.setTotalCount(positionMapper.totalCountPositionByHr(userId));
                break;
            case Constant.INTERVIEWER:
                pageBean.setData(positionMapper.selectPositionByInterviewer(userId, offset, Constant.PAGE_SIZE));
                pageBean.setTotalCount(positionMapper.totalCountPositionByInterviewer(userId));
                break;
        }
        if (pageBean.getTotalCount() % Constant.PAGE_SIZE == 0)
            pageBean.setTotalPage(pageBean.getTotalCount() / Constant.PAGE_SIZE);
        else pageBean.setTotalPage(pageBean.getTotalCount() / Constant.PAGE_SIZE + 1);
        pageBean.setNowPage(nowPage);
        return pageBean;
    }

    public boolean editPosition(Position position) {
        LambdaUpdateWrapper<Position> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Position::getPkPositionId, position.getPkPositionId());
        return update(position, lambdaUpdateWrapper);
    }

    public boolean closePosition(Position position) {
        LambdaUpdateWrapper<Position> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Position::getPkPositionId, position.getPkPositionId());
        lambdaUpdateWrapper.set(Position::getState, 0);
        return update(lambdaUpdateWrapper);
    }

    public boolean openPosition(Long positionId) {
        LambdaUpdateWrapper<Position> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Position::getPkPositionId, positionId);
        lambdaUpdateWrapper.set(Position::getState, 1);
        return update(lambdaUpdateWrapper);
    }


    public PageBean<Position> selectPositionByEs(SearchCondition searchCondition, TokenInfo tokenInfo) {
        if (searchCondition.getState() == null || searchCondition.getState() < -1 || searchCondition.getState() > 1) {
            searchCondition.setState(-1);
        }
        if (searchCondition.getPage() == null || searchCondition.getPage() < 1)
            searchCondition.setPage(1);
        if (searchCondition.getPageSize() == null || searchCondition.getPageSize() < 1)
            searchCondition.setPageSize(10);


        return searchService.searchPosition(searchCondition, tokenInfo);
    }


//    public Position getOne(Long companyId, Long positionId) {
//        // 解决缓存穿透
////        Position position = cacheClient.queryWithPassThrough(CACHE_POSITION_KEY, positionId, Position.class, this::getById, CACHE_POSITION_TTL);
//        // 利用互斥锁解决缓存击穿
//        Position position = cacheClient.queryWithMutex(CACHE_POSITION_KEY, positionId, Position.class, this::getById, CACHE_POSITION_TTL);
//        // 利用逻辑过期解决缓存击穿
////        Position position = cacheClient.queryWithLogicalExpire(CACHE_POSITION_KEY, positionId, Position.class, this::getById, CACHE_POSITION_TTL);
//        return position;
//    }
}
