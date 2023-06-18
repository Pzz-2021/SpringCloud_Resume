package com.resume.auth.mapstruct;

import com.resume.auth.dto.EnrollDTO;
import com.resume.auth.pojo.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/*
 *@filename: Company
 *@author: lyh
 *@date:2023/6/18 9:24
 *@version 1.0
 *@description TODO
 */
@Mapper
public interface CompanyMapstruct {
    CompanyMapstruct INSTANCT = Mappers.getMapper(CompanyMapstruct.class);

    Company conver(EnrollDTO enrollDTO);
}
