package com.resume.parse.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 *@filename: QiNiuProperties
 *@author: lyh
 *@date:2023/3/22 22:09
 *@version 1.0
 *@description TODO
 */

@Data
@Component//将@ConfigurationProperties注解的类注入到spring容器中
@ConfigurationProperties(prefix="qi-niu-upload")//读取配置注入
public class QiNiuProperties {
    private String accessKey;
    private String accessSecret;
    private String bucketName;
    private String hostName;
}
