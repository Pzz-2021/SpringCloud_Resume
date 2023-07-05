package com.resume.gateway.filter;

/*
 *@filename: AuthGlobalFilter
 *@author: lyh
 *@date:2023/6/12 2:02
 *@version 1.0
 *@description 自定义全局过滤器
 */

import com.alibaba.fastjson.JSON;
import com.resume.base.utils.Constant;
import com.resume.base.utils.JwtUtil;
import com.resume.gateway.response.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {
    //白名单
    protected static Set<String> whitelist = null;
    static {
        //加载白名单
        try (
             InputStream resourceAsStream = TokenGlobalFilter.class.getResourceAsStream("/whitelist.properties")
        ) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Set<String> strings = properties.stringPropertyNames();
            whitelist= new HashSet<>(strings);

        } catch (Exception e) {
            log.error("加载/security-whitelist.properties出错:{}",e.getMessage());
            e.printStackTrace();
        }
    }
    //过滤器逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().value();
        //白名单放行
        if(whitelist.contains(requestUrl))return chain.filter(exchange);
        //检查token是否存在
        String token = getToken(exchange);
        if (StringUtils.isBlank(token)) {
            return buildReturnMono(Constant.UNAUTHORIZED_EXCEPTION_CODE,Constant.UNAUTHORIZED_EXCEPTION,exchange);
        }
         //判断token是否有效
        String result = JwtUtil.checkToken(token);
        switch (result){
            case Constant.EXPIRED_JWT_EXCEPTION:
                // TODO:返回给前端，前端需要加上响应拦截器，发现code是555即需重新发请求刷新token
                return buildReturnMono(Constant.EXPIRED_JWT_EXCEPTION_CODE,Constant.EXPIRED_JWT_EXCEPTION,exchange);
            case Constant.EXCEPTION:
                return buildReturnMono(Constant.UNAUTHORIZED_EXCEPTION_CODE,Constant.EXCEPTION,exchange);
        }
        //从token里面拿用户id并存到request header中，方便鉴权过滤器拿
        Consumer<HttpHeaders> httpHeaders = httpHeader -> httpHeader.set("userId", JwtUtil.getUserId(token));
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        exchange.mutate().request(serverHttpRequest).build();
        //  放行
        return chain.filter(exchange);
    }
    //出现异常，不再路由，直接返回给前端
    public Mono<Void> buildReturnMono(int errorCode,String error, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        String jsonString = JSON.toJSONString(new RestErrorResponse(errorCode,error));
        byte[] bits = jsonString.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //设置响应状态码
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
    /**
     * 获取token，以"Bearer "为前缀
     */
    public String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(tokenStr)) {
            return null;
        }
        //使用空格（" "）作为分隔符将tokenStr字符串分割成一个字符串数组
        //并从该数组中获取第二个元素（即索引为1的元素）
        String token = tokenStr.split(" ")[1];
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token;
    }

    //标识当前过滤器的优先级，返回值越小，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
