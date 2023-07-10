package com.resume.parse.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.base.utils.DateUtil;
import com.resume.parse.dto.PhasedOutDTO;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.parse.pojo.Resume;
import com.resume.parse.utils.OCRUtil;
import com.resume.parse.utils.RedisConstants;
import com.resume.parse.utils.URLUtil;
import com.resume.parse.utils.UploadUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pp
 * @since 2023-07-04
 */
@Service
public class ResumeService extends ServiceImpl<ResumeMapper, Resume> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UploadUtil uploadUtil;

    private static final String PATH = "http://flaskService";

    private static final String PHASEDOUT = "已淘汰";

    private static final String PDF_PATH = "D:/code/pythonProject1/uie_v1/data/temp/";

    public void parseResume(Long pkResumeId, String url) {
        Resume resume = new Resume();
        resume.setPkResumeId(pkResumeId);

        String text = "";
        String pdfUrl = "";

        // 获取简历文字信息
        String fileExtension = URLUtil.getFileExtension(url);
        switch (fileExtension) {
            case ".jpeg":
            case ".jpg":
            case ".png":
                text = OCRUtil.parseImage(url);
                break;
            case ".pdf":
                text = restTemplate.getForObject(PATH + "/get-word-string?url=" + url, String.class);
                pdfUrl = url;
                break;
            case ".word":
                text = restTemplate.getForObject(PATH + "/get-word-string?url=" + url, String.class);

                try {
                    Path path = Paths.get(PDF_PATH + URLUtil.getFileBaseName(url) + ".pdf");
                    byte[] fileBytes = Files.readAllBytes(path);
                    pdfUrl = uploadUtil.uploadByBytes(fileBytes, UUID.randomUUID().toString() + '-' + URLUtil.getFileBaseName(url) + ".pdf");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        resume.setResumeContent(text);
        resume.setPdfUrl(pdfUrl);


        // 将文字信息解析，得到 JSON 的String信息
        String jsonString = restTemplate.getForObject(PATH + "/parseString?txt=" + text, String.class);

        // Unicode 转义的 JSON 数据 转义回来
        String json = StringEscapeUtils.unescapeJava(jsonString);

        // 解析为JSON数据
        JSONObject jsonObject = JSONObject.parseObject(json);

        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();

        for (Map.Entry<String, Object> entry : entries) {
            // 得到基础信息，保存数据库
            if (entry.getKey().equals("resume")) {
                JSONArray value = (JSONArray) entry.getValue();
                resume = BeanUtil.fillBeanWithMap((Map<String, Object>) value.get(0), resume, false);
//                System.out.println("resume = " + resume);

            } else {
                // 其他信息
                System.out.println(entry.getValue());
            }
        }

        // 把基础信息移除
        jsonObject.remove("resume");

        resume.setJsonContent(String.valueOf(jsonObject));

        // 设置resume状态已解析
        resume.setIsParsed(1);

        this.updateById(resume);
    }


    public boolean changeResumeState(Long resumeId, String targetState, String phasedOutCause) {
        LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Resume::getPkResumeId, resumeId);
        updateWrapper.set(Resume::getState, targetState);
        updateWrapper.set(Resume::getUpdateTime, DateUtil.getDate2());
        if (PHASEDOUT.equals(targetState)) updateWrapper.set(Resume::getPhasedOutCause, phasedOutCause);
        return update(updateWrapper);
    }

    public boolean phasedOutResume(PhasedOutDTO phasedOutDTO) {
        return changeResumeState(phasedOutDTO.getResumeId(), PHASEDOUT, phasedOutDTO.getPhasedOutCause());
    }
}
