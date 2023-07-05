package com.resume.gateway.filter;


import com.resume.base.utils.Constant;
import com.resume.gateway.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/*
 *@filename: AuthGlobalFilter2
 *@author: lyh
 *@date:2023/6/13 10:13
 *@version 1.0
 *@description 鉴权
 */
@Component
@Slf4j
public class AccessGlobalFilter extends TokenGlobalFilter {

    @Autowired
    private RedisUtil redisUtil;
    //特殊鉴权名单
    protected static Set<String> limitAccessList = null;
    static {
        //加载特殊鉴权名单
        try (
                InputStream resourceAsStream = TokenGlobalFilter.class.getResourceAsStream("/limit-access.properties")
        ) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Set<String> strings = properties.stringPropertyNames();
            limitAccessList= new HashSet<>(strings);
        } catch (Exception e) {
            log.error("加载/limit-access.properties出错:{}",e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.判断当前url是否需要忽略
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        //白名单放行
        if(whitelist.contains(requestUrl))return chain.filter(exchange);
        //如果是需要刷新refresh_token的请求那么不需要鉴权
        if(pathMatcher.match("/auth/refresh-token",requestUrl))return chain.filter(exchange);
        //2.判断当前url是否需要特殊鉴权
        if(limitAccessList.contains(requestUrl)){
            //符合特殊鉴权名单
            //获取请求方式和URL并拼接
            String method = exchange.getRequest().getMethodValue();
            String permission=method+":"+requestUrl;
            //3.从缓存中获取鉴权资源，同样是由请求方式和URL并拼接
            String key=Constant.USER_KEY + exchange.getRequest().getHeaders().get("userId");
            //4.当前用户没有权限
            if(redisUtil.sHasKey(key,permission)) buildReturnMono(Constant.INSUFFICIENT_PERMISSIONS_CODE, Constant.INSUFFICIENT_PERMISSIONS, exchange);
            //5.当前用户拥有权限
            return chain.filter(exchange);
        }
        //  放行
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return 10;
    }
}
