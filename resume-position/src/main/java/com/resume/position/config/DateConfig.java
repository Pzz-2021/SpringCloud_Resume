package com.resume.position.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.resume.base.utils.DateUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 *@filename: DateConfig
 *@author: lyh
 *@date:2023/8/15 19:54
 *@version 1.0
 *@description TODO
 */
@Component
public class DateConfig implements MetaObjectHandler {

    /**
     * 使用mp做添加操作时候，这个方法执行
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        this.setFieldValByName("createTime", DateUtil.getDate2(),metaObject);
        this.setFieldValByName("updateTime",DateUtil.getDate2(),metaObject);
    }

    /**
     * 使用mp做修改操作时候，这个方法执行
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",DateUtil.getDate2(),metaObject);
    }
}
