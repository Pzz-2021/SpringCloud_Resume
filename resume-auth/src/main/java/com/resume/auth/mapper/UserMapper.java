package com.resume.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.auth.pojo.Menu;
import com.resume.auth.pojo.Operation;
import com.resume.auth.pojo.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
     List<Operation> getUserPermissions(Long userId);

     List<Menu>getMenus(Long userId);
}
