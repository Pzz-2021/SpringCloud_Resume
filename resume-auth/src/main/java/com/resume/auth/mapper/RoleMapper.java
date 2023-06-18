package com.resume.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.auth.pojo.Role;
import org.springframework.stereotype.Component;

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
}
