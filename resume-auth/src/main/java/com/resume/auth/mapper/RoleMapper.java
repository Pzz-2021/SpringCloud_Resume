package com.resume.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.auth.dto.MemberDTO;
import com.resume.auth.pojo.Role;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-06-18
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {
      void addCompanyAdmin(Long userId);

      String selectUserRole(Long userId);

      void addCompanyHr(Long userId);

      void addCompanyInterviewer(Long userId);

      List<MemberDTO>selectTeamAdmin(Long companyId);
      List<MemberDTO>selectTeamOtherMember(Long companyId);

//      void deleteTeamMembers(Long userId);
}
