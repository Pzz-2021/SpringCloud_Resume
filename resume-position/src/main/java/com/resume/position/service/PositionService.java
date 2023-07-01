package com.resume.position.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.position.pojo.Position;
import com.resume.position.mapper.PositionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.position.utils.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.resume.position.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 彭政
 * @since 2023-06-18
 */
@Service
public class PositionService extends ServiceImpl<PositionMapper, Position>  {

    @Autowired
    private CacheClient cacheClient;


    public Position getOne(Long companyId, Long positionId) {
        // 解决缓存穿透
//        Position position = cacheClient.queryWithPassThrough(CACHE_POSITION_KEY, positionId, Position.class, this::getById, CACHE_POSITION_TTL);
        // 利用互斥锁解决缓存击穿
        Position position = cacheClient.queryWithMutex(CACHE_POSITION_KEY, positionId, Position.class, this::getById, CACHE_POSITION_TTL);
        // 利用逻辑过期解决缓存击穿
//        Position position = cacheClient.queryWithLogicalExpire(CACHE_POSITION_KEY, positionId, Position.class, this::getById, CACHE_POSITION_TTL);
        return position;
    }

    public void selectPositionByPage(TokenInfo tokenInfo) {
        Long userId=tokenInfo.getPkUserId();
        Long companyId=tokenInfo.getCompanyId();
        switch (tokenInfo.getRole()){
            case Constant.COMPANY_ADMIN:

            case Constant.HR:

            case Constant.INTERVIEWER:
        }
    }
}
