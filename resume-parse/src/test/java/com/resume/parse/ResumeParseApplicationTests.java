package com.resume.parse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ResumeParseApplicationTests {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate rest = new RestTemplate(requestFactory);

        String forObject = rest.getForObject("http://position-service/position/test?name=pzz", String.class);

        System.out.println(forObject);


    }

}
