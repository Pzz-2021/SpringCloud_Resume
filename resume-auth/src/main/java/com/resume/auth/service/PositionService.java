package com.resume.auth.service;

/*
 *@filename: DubboService
 *@author: lyh
 *@date:2023/7/5 11:54
 *@version 1.0
 *@description TODO
 */

import com.resume.auth.mapper.RoleMapper;
import com.resume.dubbo.api.UserService;
import com.resume.dubbo.domian.MemberDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class PositionService implements UserService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<MemberDTO> getCompanyTeam(Long companyId) {
        return roleMapper.selectTeamOtherMember(companyId);
    }
}
