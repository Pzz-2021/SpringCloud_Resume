package com.resume.position.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.DateUtil;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.*;
import com.resume.dubbo.domian.HomeVo;
import com.resume.position.mapper.PositionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.position.mapstruct.PosistionMapstruct;
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
@DubboService
public class PositionService extends ServiceImpl<PositionMapper, Position> implements com.resume.dubbo.api.PositionService {

    @Autowired
    private CacheClient cacheClient;

    @DubboReference
    private SearchService searchService;

    @Resource
    private PositionMapper positionMapper;

    @Autowired
    private PositionTeamService positionTeamService;


    @Override
    public HomeVo getHome(Long companyId) {
        HomeVo homeVo = new HomeVo();

        // 职位数据
        List<Position> list = this.list(new LambdaQueryWrapper<Position>().eq(Position::getCompanyId, companyId));
        list.forEach(position -> {
            if (position.getState() == 1)
                homeVo.setPositioningCount(homeVo.getPositioningCount() + 1);
            homeVo.setPreEmployCount(homeVo.getPreEmployCount() + position.getPendEmploy());
            homeVo.setAfterEmployCount(homeVo.getAfterEmployCount() + position.getEmployedEmploy());
            homeVo.setTargetCount(homeVo.getTargetCount() + position.getHc());
        });

        return homeVo;
    }

    // 添加职位
    public boolean addPosition(TokenInfo tokenInfo, Position position) {
        position.setCompanyId(tokenInfo.getCompanyId());
        position.setCreateUserId(tokenInfo.getPkUserId());
        position.setFirstScreenerCount(0);
        position.setInterviewCount(0);
        position.setCommunicateOfferCount(0);
        position.setPendEmploy(0);
        position.setEmployedEmploy(0);
        position.setCreateTime(DateUtil.getDate2());
        position.setUpdateTime(DateUtil.getDate2());
        position.setState(1);

        boolean save = this.save(position);
        System.out.println(position.toString());
        System.out.println("==========================");
        // 添加成功保存至 es 并添加职位负责人
        if (save) {
            if (Constant.HR.equals(tokenInfo.getRole())) {
                PositionTeam positionTeam = new PositionTeam();
                positionTeam.setPositionId(position.getPkPositionId());
                positionTeam.setRoleId(2);
                positionTeam.setRoleName(Constant.HR);
                positionTeam.setUserId(position.getCreateUserId());
                positionTeam.setCreateTime(DateUtil.getDate2());
                positionTeamService.save(positionTeam);
            }

            PositionDTO positionDTO = PosistionMapstruct.INSTANCT.conver(position);
            positionDTO.setPositionTeamIdList(positionMapper.selectPositionTeam(position.getPkPositionId()));

            searchService.savePositionDTO(positionDTO);
        }

        return save;
    }

    // 修改职位
    public boolean editPosition(Position position) {
        boolean b = updateById(position);
        // 修改成功 同步至es
        if (b) {
            PositionDTO positionDTO = PosistionMapstruct.INSTANCT.conver(position);
            positionDTO.setPositionTeamIdList(positionMapper.selectPositionTeam(position.getPkPositionId()));
            searchService.updatePositionDTOById(positionDTO);
        }
        return b;
    }

    // 关闭职位也是一种修改职位，是修改职位的一种特例
    public boolean closePosition(Long positionId) {
        Position position = new Position(positionId, 0);
        return editPosition(position);
    }

    // 激活职位也是一样
    public boolean openPosition(Long positionId) {
        Position position = new Position(positionId, 1);
        return editPosition(position);
    }

    // 使用 es 分页搜索
    public PageBean<Position> selectPositionByEs(SearchCondition searchCondition, TokenInfo tokenInfo) {
        if (searchCondition.getState() == null || searchCondition.getState() < -1 || searchCondition.getState() > 1)
            searchCondition.setState(-1);
        if (searchCondition.getPage() == null || searchCondition.getPage() < 1)
            searchCondition.setPage(1);
        if (searchCondition.getPageSize() == null || searchCondition.getPageSize() < 1)
            searchCondition.setPageSize(-1);

        return searchService.searchPosition(searchCondition, tokenInfo);
    }



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

    public Position getOneById(Long positionId) {
        return searchService.getPositionById(positionId);
    }


    @Override
    public int changePositionResumeCount(ResumeStateDTO resumeStateDTO) {
        return positionMapper.changePositionResumeCount(resumeStateDTO);
    }

    @Override
    public int addCandidateNum(Long positionId) {
        positionMapper.addCandidateNum(positionId);
        return positionMapper.getCandidateNum(positionId);
    }

    @Override
    public List<PositionTeam> queryOptionalInterviewer(Long positionId) {
        return positionTeamService.getSelectedPositionTeam(positionId);
    }

    @Override
    public Position getOne(Long positionId) {
        return positionMapper.selectById(positionId);
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
