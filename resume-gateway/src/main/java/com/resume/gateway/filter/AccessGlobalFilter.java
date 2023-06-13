package com.resume.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.resume.base.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/*
 *@filename: AuthGlobalFilter2
 *@author: lyh
 *@date:2023/6/13 10:13
 *@version 1.0
 *@description 鉴权
 */
@Component
public class AccessGlobalFilter extends TokenGlobalFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.判断当前url是否需要忽略
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        //白名单放行
        for (String url : whitelist) {
            //符合白名单
            if (pathMatcher.match(url, requestUrl)) {
                return chain.filter(exchange);
            }
        }
        //2.获取请求方式和URL并拼接
        String method = exchange.getRequest().getMethodValue();
        String permission=method+requestUrl;
        //3.从缓存中获取鉴权资源，同样是由请求方式和URL并拼接
        String key=Constant.USER_KEY + exchange.getRequest().getHeaders().get("userId");
        //-1表示最后一个元素
        List<String> permissionList=stringRedisTemplate.opsForList().range(key, 0, -1);
        long count=permissionList.stream().filter(resource-> resource.startsWith(permission)).count();
        //4.当前用户没有权限
        if(count<=0)buildReturnMono(Constant.INSUFFICIENT_PERMISSIONS,exchange);
        //5.当前用户拥有权限
        //  放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
