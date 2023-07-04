package com.resume.position.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.DateUtil;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.PositionDTO;
import com.resume.position.mapper.PositionMapper;
import com.resume.position.mapper.PositionTeamMapper;
import com.resume.position.pojo.PositionTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-07-03
 */
@Service
public class PositionTeamService extends ServiceImpl<PositionTeamMapper, PositionTeam> {

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private SearchService searchService;

    // 添加一个职位负责人
    public boolean addPositionTeam(PositionTeam positionTeam) {
        positionTeam.setCreateTime(DateUtil.getDate2());
        boolean save = this.save(positionTeam);

        // 添加成功同步至es
        if (save)
            updateEsByPositionId(positionTeam.getPositionId());

        return save;
    }


    public boolean deletePositionTeam(Long positionId, Long userId) {
        LambdaQueryWrapper<PositionTeam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PositionTeam::getPositionId, positionId)
                .eq(PositionTeam::getUserId, userId);

        boolean b = this.remove(queryWrapper);

        if (b)
            updateEsByPositionId(positionId);

        return b;
    }

    // 更新es中职位的 负责人(HR or 面试官)
    private void updateEsByPositionId(Long positionId){
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setPkPositionId(positionId);
        positionDTO.setPositionTeamIdList(positionMapper.selectPositionTeam(positionId));

        searchService.updatePositionDTOById(positionDTO);
    }
}
