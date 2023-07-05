package com.resume.position.service;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NacosUrlService {
    /**
     * 注入LoadBalancerClient
     */
    @Resource
    private LoadBalancerClient loadBalancerClient;

    /**
     * 通过负载均衡方式获取nacos上注册的某个xxx实例
     *
     * @return
     */
    public ServiceInstance getServiceInstance(String name) {
        //这里的xxx即为nacos控制面板上的服务名
        ServiceInstance instance = loadBalancerClient.choose(name);
        instance.getHost();
        instance.getPort();
        return instance;
    }
}
