package com.resume.parse.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.parse.pojo.Resume;
import com.resume.parse.utils.RedisConstants;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
@Service
public class ResumeService extends ServiceImpl<ResumeMapper, Resume> {

    @Autowired
    private RestTemplate restTemplate;


    public void parseResume(Long pkResumeId, String url) {
        Resume resume = new Resume();
        resume.setPkResumeId(pkResumeId);

        // 文字信息
        String text = restTemplate.getForObject("http://localhost:5000/get-word-string?url=" + url, String.class);
//        System.out.println(text);
        resume.setResumeContent(text);

        // 将文字信息解析，得到 JSON 的String信息
        text = restTemplate.getForObject("http://localhost:5000/parseString?txt=" + text, String.class);
//        System.out.println("\n\n" + text);

        // Unicode 转义的 JSON 数据 转义回来
        String json = StringEscapeUtils.unescapeJava(text);

        JSONObject jsonObject = JSONObject.parseObject(json);
//        System.out.println(jsonObject);

        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            // 得到基础信息
            if (entry.getKey().equals("resume")) {
                JSONArray value = (JSONArray) entry.getValue();
                resume = BeanUtil.fillBeanWithMap((Map<String, Object>) value.get(0), resume, false);
//                System.out.println("resume = " + resume);

            } else {
                // 其他信息
                System.out.println(entry.getValue());
            }
        }

        jsonObject.remove("resume");
//        System.out.println(jsonObject);
        resume.setJsonContent(String.valueOf(jsonObject));
    }
}
