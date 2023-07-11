package com.resume.dubbo.api;

import com.resume.dubbo.domian.MemberDTO;

import java.util.List;

/*
 *@filename: UserService
 *@author: lyh
 *@date:2023/7/5 9:48
 *@version 1.0
 *@description TODO
 */
public interface UserService {

    List<MemberDTO>getCompanyTeam(Long company);


}
