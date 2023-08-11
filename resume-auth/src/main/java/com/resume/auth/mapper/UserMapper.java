package com.resume.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.auth.pojo.Menu;
import com.resume.auth.pojo.Operation;
import com.resume.auth.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface UserMapper extends BaseMapper<User> {
     List<Operation> getUserPermissions(Long userId);

     List<Menu>getMenus(Long userId);

     User getDeletedUser(String userEmail);
}
