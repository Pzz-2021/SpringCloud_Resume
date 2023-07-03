package com.resume.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.auth.mapper.CompanyMapper;
import com.resume.auth.pojo.Company;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *@filename: CompanyService
 *@author: lyh
 *@date:2023/6/18 9:45
 *@version 1.0
 *@description TODO
 */
@Service
@Getter
public class CompanyService extends ServiceImpl<CompanyMapper, Company> {
    @Autowired
    private CompanyMapper companyMapper;

}
