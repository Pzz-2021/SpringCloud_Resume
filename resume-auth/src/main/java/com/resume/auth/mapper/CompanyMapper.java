package com.resume.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.auth.pojo.Company;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-06-18
 */
//防止注入爆红
@Component
public interface CompanyMapper extends BaseMapper<Company> {

}
