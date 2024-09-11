package com.resume.framework.starter.idempotent.config;

import com.resume.framework.starter.idempotent.core.NoDuplicateSubmitAspect;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * 幂等组件相关配置类
 * <p>
 * 作者：PP
 * 开发时间：2024-08-10
 */
public class IdempotentConfiguration {

    /**
     * 防止用户重复提交表单信息切面控制器
     */
    @Bean
    public NoDuplicateSubmitAspect noDuplicateSubmitAspect(RedissonClient redissonClient) {
        return new NoDuplicateSubmitAspect(redissonClient);
    }
}