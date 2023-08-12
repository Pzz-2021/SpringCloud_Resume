package com.resume.position.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.base.utils.DateUtil;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.api.UserService;
import com.resume.dubbo.domian.MemberDTO;
import com.resume.dubbo.domian.PositionDTO;
import com.resume.position.mapper.PositionMapper;
import com.resume.position.mapper.PositionTeamMapper;
import com.resume.position.pojo.PositionTeam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private PositionMapper positionMapper;

    @DubboReference
    private SearchService searchService;

    @Resource
    private PositionTeamMapper positionTeamMapper;

    @DubboReference
    private UserService userService;

    // 添加一个职位负责人
    public boolean addPositionTeam(PositionTeam positionTeam) {
        positionTeam.setCreateTime(DateUtil.getDate2());
        boolean save = this.save(positionTeam);
        // 添加成功同步至es
        if (save) updateEsByPositionId(positionTeam.getPositionId());
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
    private void updateEsByPositionId(Long positionId) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setPkPositionId(positionId);
        positionDTO.setPositionTeamIdList(positionMapper.selectPositionTeam(positionId));
        searchService.updatePositionDTOById(positionDTO);
    }

    public List<MemberDTO> getOptionalPositionTeam(Long positionId, Long companyId) {
        //已选择的负责人（HR 面试官）
        List<Long> selectedPositionTeamMemberId = positionTeamMapper.getSelectedPositionTeamMemberId(positionId);
        //公司的团队所有人（HR 面试官）
        List<MemberDTO> companyTeam = userService.getCompanyTeam(companyId);
        //过滤
        return companyTeam.stream()
                .filter(member -> !selectedPositionTeamMemberId.contains((member.getPkUserId()))).collect(Collectors.toList());
    }

    public List<PositionTeam> getSelectedPositionTeam(Long positionId) {
        List<PositionTeam> selectedPositionTeam = positionTeamMapper.getSelectedPositionTeam(positionId);
        selectedPositionTeam.sort(new Comparator<PositionTeam>() {
            @Override
            public int compare(PositionTeam o1, PositionTeam o2) {
                String role1 = o1.getRoleName();
                String role2 = o2.getRoleName();
                return role1.compareTo(role2);
            }
        });
        return selectedPositionTeam;
    }

}
