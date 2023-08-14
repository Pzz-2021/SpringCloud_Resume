package com.resume.parse.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.DateUtil;
import com.resume.dubbo.api.PositionService;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.ResumeStateDTO;
import com.resume.dubbo.domian.SearchCondition;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.dubbo.domian.Resume;
import com.resume.parse.utils.OCRUtil;
import com.resume.parse.utils.URLUtil;
import com.resume.parse.utils.UploadUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

    @DubboReference
    private SearchService searchService;

    @DubboReference
    private PositionService positionService;

    private static final String PATH = "http://flaskService";

    private static final String PDF_PATH = "D:/code/pythonProject1/uie_v1/data/temp/";

    public boolean removeResume(ResumeStateDTO resumeStateDTO){
        //添加
        LambdaUpdateWrapper<Resume>updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Resume::getPkResumeId,resumeStateDTO.getResumeId());
        Resume resume=new Resume();
        resume.setPositionId(resumeStateDTO.getPositionId());
        resume.setPositionName(resumeStateDTO.getPositionName());
        resume.setState(Constant.FIRST_SCREENER);
        update(resume,updateWrapper);
        //修改职位统计数量
        boolean save= positionService.addCandidateNum(resumeStateDTO.getPositionId());
        //同步es
        if(save)searchService.updateResumeById(this.getById(resumeStateDTO.getResumeId()));
        //修改职位状态统计数量
        return save;
    }

    public Resume getOneByEs(Long pkResumeId) {
        return searchService.getResumeById(pkResumeId);
    }

    public PageBean<Resume> selectResumeByEs(SearchCondition searchCondition, TokenInfo tokenInfo) {
        if (searchCondition.getPage() == null || searchCondition.getPage() == 0)
            searchCondition.setPage(1);
        if (searchCondition.getPageSize() == null || searchCondition.getPageSize() == 0)
            searchCondition.setPageSize(10);
        if (searchCondition.getState() == null)
            searchCondition.setState(0);

        return searchService.searchResume(searchCondition, tokenInfo);
    }


    public void parseResume(Long pkResumeId, String url) {
        Resume resume = new Resume();
        resume.setPkResumeId(pkResumeId);

        String text = "";
        String pdfUrl = "";

        // 获取简历文字信息
        String fileExtension = URLUtil.getFileExtension(url);
        switch (fileExtension) {
            case "txt":
                try {
                    // 创建URL对象
                    URL urlFile = new URL(url);

                    // 打开URL连接
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlFile.openStream()));
                    String line;
                    StringBuilder content = new StringBuilder();

                    // 逐行读取文件内容并追加到StringBuilder中
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }

                    reader.close();

                    // 将StringBuilder转换为String
                    text = content.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "jpeg":
            case "jpg":
            case "png":
                text = OCRUtil.parseImage(url);
                break;
            case "pdf":
                text = restTemplate.getForObject(PATH + "/get-word-string?url=" + url, String.class);
                pdfUrl = url;
                break;
            case "doc":
            case "docx":
                text = restTemplate.getForObject(PATH + "/get-word-string?url=" + url, String.class);

                // 上传pdf文件
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
                // 将基础信息转化为Bean 存入数据库
                resume = BeanUtil.fillBeanWithMap((Map<String, Object>) value.get(0), resume, false);
            } else {
                // 其他信息
                System.out.println(entry.getValue());
            }
        }

        // 把基础信息移除，并放入JsonContent至数据库中
        jsonObject.remove("resume");
        resume.setJsonContent(String.valueOf(jsonObject));

        // 设置resume状态已解析
        resume.setIsParsed(1);

        boolean b = this.updateById(resume);

        // 同步 es
        if (b) {
            searchService.saveResume(this.getById(resume.getPkResumeId()));
        }
    }

    public int changePositionResumeCount(ResumeStateDTO resumeStateDTO) {
        int count = positionService.changePositionResumeCount(resumeStateDTO);

        // 同步 es
        if (count > 0) {
            searchService.updateResumeById(this.getById(resumeStateDTO.getResumeId()));
        }

        return count;
    }

    public boolean changeResumeState(ResumeStateDTO resumeStateDTO) {
        LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Resume::getPkResumeId, resumeStateDTO.getResumeId());
        updateWrapper.set(Resume::getState, resumeStateDTO.getTargetState());
        updateWrapper.set(Resume::getUpdateTime, DateUtil.getDate2());
        if (Constant.OBSOLETE.equals(resumeStateDTO.getTargetState())) {
            updateWrapper.set(Resume::getPhasedOutCause, resumeStateDTO.getPhasedOutCause());
        }
        update(updateWrapper);
        return changePositionResumeCount(resumeStateDTO) > 0;
    }

    public boolean phasedOutResume(ResumeStateDTO resumeStateDTO) {
        return changeResumeState(resumeStateDTO);
    }

}
