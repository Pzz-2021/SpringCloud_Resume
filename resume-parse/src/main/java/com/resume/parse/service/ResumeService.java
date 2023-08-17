package com.resume.parse.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.DateUtil;
import com.resume.dubbo.api.PositionService;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.*;
import com.resume.dubbo.domian.RemoveResumeDTO;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.parse.pojo.Interview;
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
import java.time.LocalDate;
import java.util.List;
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

    @Autowired
    private InterviewService interviewService;


    private static final String PATH = "http://flaskService";

    private static final String PDF_PATH = "D:/code/pythonProject1/uie_v1/data/temp/";

    public void removeResume(RemoveResumeDTO removeResumeDTO) {
        Resume resume = new Resume();
        int newCandidateNum;
        if(removeResumeDTO.getPreState().equals(Constant.UNCHECKED)){
            //修改职位统计数量
            newCandidateNum = positionService.addCandidateNum(removeResumeDTO.getPrePositionId());
        }else{
            //减少
            positionService.decreaseCandidateNum(removeResumeDTO);
            PositionDTO positionDTO=new PositionDTO();
            Position position=positionService.getOne(removeResumeDTO.getPrePositionId());
            positionDTO.setPkPositionId( position.getPkPositionId() );
            positionDTO.setCompanyId( position.getCompanyId() );
            positionDTO.setPositionName( position.getPositionName() );
            positionDTO.setCreateUserId( position.getCreateUserId() );
            positionDTO.setDescription( position.getDescription() );
            positionDTO.setHc( position.getHc() );
            positionDTO.setWorkingCity( position.getWorkingCity() );
            positionDTO.setWorkingYears( position.getWorkingYears() );
            positionDTO.setEducationBackground( position.getEducationBackground() );
            positionDTO.setType( position.getType() );
            positionDTO.setSalaryMin( position.getSalaryMin() );
            positionDTO.setSalaryMax( position.getSalaryMax() );
            positionDTO.setSalaryMonth( position.getSalaryMonth() );
            positionDTO.setFirstScreenerCount( position.getFirstScreenerCount() );
            positionDTO.setInterviewCount( position.getInterviewCount() );
            positionDTO.setCommunicateOfferCount( position.getCommunicateOfferCount() );
            positionDTO.setPendEmploy( position.getPendEmploy() );
            positionDTO.setEmployedEmploy( position.getEmployedEmploy() );
            positionDTO.setCreateTime( position.getCreateTime() );
            positionDTO.setUpdateTime( position.getUpdateTime() );
            positionDTO.setState( position.getState() );
            searchService.updatePositionDTOById(positionDTO);
            //增加
            newCandidateNum = positionService.addCandidateNum(removeResumeDTO.getTargetPositionId());
        }
        //同步es职位
        PositionDTO positionDTO=new PositionDTO();
        positionDTO.setPkPositionId(removeResumeDTO.getTargetPositionId());
        positionDTO.setFirstScreenerCount(newCandidateNum);
        searchService.updatePositionDTOById(positionDTO);
        //同步es简历
        searchService.updateResumeById(resume);
        //修改 简历所属职位
        LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Resume::getPkResumeId, removeResumeDTO.getResumeId());
        resume.setPkResumeId(removeResumeDTO.getResumeId());
        resume.setPositionId(removeResumeDTO.getTargetPositionId());
        resume.setPositionName(removeResumeDTO.getTargetPositionName());
        resume.setState(Constant.FIRST_SCREENER);
        update(resume, updateWrapper);

    }

    public Resume getOneByEs(Long pkResumeId) {
        return searchService.getResumeById(pkResumeId);
    }


    public PageBean<Resume> selectResumeByEs(SearchCondition searchCondition, TokenInfo tokenInfo, Long positionId) {
        if (searchCondition.getPage() == null || searchCondition.getPage() == 0)
            searchCondition.setPage(1);
        if (searchCondition.getPageSize() == null || searchCondition.getPageSize() == 0)
            searchCondition.setPageSize(10);
        if (searchCondition.getState() == null)
            searchCondition.setState(0);
        if (positionId == null)
            positionId = 0L;

        if (positionId != 0) {
            Position position = positionService.getOne(positionId);
            searchCondition.setQuery(position.getPositionName() + " " + position.getDescription());
        }

        return searchService.searchResume(searchCondition, tokenInfo, positionId);
    }


    public List<Resume> getResumeByPosition(Long positionId) {
        TokenInfo tokenInfo = new TokenInfo(1L, 1L, "超级管理员");
        PageBean<Resume> resumePageBean = selectResumeByEs(new SearchCondition(), tokenInfo, positionId);
        return resumePageBean.getData();
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
//                System.out.println(entry.getValue());
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
            //更新简历
            searchService.updateResumeById(this.getById(resumeStateDTO.getResumeId()));
            //更新职位
            Position position=positionService.getOne(resumeStateDTO.getPositionId());
//            PositionDTO positionDTO= PosistionMapstruct.INSTANCT.conver(position);
            PositionDTO positionDTO=new PositionDTO();
            positionDTO.setPkPositionId( position.getPkPositionId() );
            positionDTO.setCompanyId( position.getCompanyId() );
            positionDTO.setPositionName( position.getPositionName() );
            positionDTO.setCreateUserId( position.getCreateUserId() );
            positionDTO.setDescription( position.getDescription() );
            positionDTO.setHc( position.getHc() );
            positionDTO.setWorkingCity( position.getWorkingCity() );
            positionDTO.setWorkingYears( position.getWorkingYears() );
            positionDTO.setEducationBackground( position.getEducationBackground() );
            positionDTO.setType( position.getType() );
            positionDTO.setSalaryMin( position.getSalaryMin() );
            positionDTO.setSalaryMax( position.getSalaryMax() );
            positionDTO.setSalaryMonth( position.getSalaryMonth() );
            positionDTO.setFirstScreenerCount( position.getFirstScreenerCount() );
            positionDTO.setInterviewCount( position.getInterviewCount() );
            positionDTO.setCommunicateOfferCount( position.getCommunicateOfferCount() );
            positionDTO.setPendEmploy( position.getPendEmploy() );
            positionDTO.setEmployedEmploy( position.getEmployedEmploy() );
            positionDTO.setCreateTime( position.getCreateTime() );
            positionDTO.setUpdateTime( position.getUpdateTime() );
            positionDTO.setState( position.getState() );
            searchService.updatePositionDTOById(positionDTO);
        }
        return count;
    }

    public boolean changeResumeState(ResumeStateDTO resumeStateDTO) {
        LambdaUpdateWrapper<Resume> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Resume::getPkResumeId, resumeStateDTO.getResumeId());
        updateWrapper.set(Resume::getState, resumeStateDTO.getTargetState());
        if (Constant.OBSOLETE.equals(resumeStateDTO.getTargetState())) {
            updateWrapper.set(Resume::getPhasedOutCause, resumeStateDTO.getPhasedOutCause());
        }
        update(updateWrapper);
        return changePositionResumeCount(resumeStateDTO) > 0;
    }

    public boolean phasedOutResume(ResumeStateDTO resumeStateDTO) {
        return changeResumeState(resumeStateDTO);
    }

    public HomeVo getHome(Long companyId) {
        HomeVo homeVo = positionService.getHome(companyId);

        LocalDate mondayOfWeek = DateUtil.getMondayOfWeek(LocalDate.now());
        List<Resume> resumeList = this.list(new LambdaQueryWrapper<Resume>().eq(Resume::getCompanyId, companyId).between(Resume::getCreateTime, mondayOfWeek.toString(), mondayOfWeek.plusDays(7).toString()));
        homeVo.setNewResumeCount(resumeList.size());

        List<Interview> interviewList = interviewService.list(new LambdaQueryWrapper<Interview>().eq(Interview::getCompanyId, companyId).between(Interview::getStartDate, mondayOfWeek.toString(), mondayOfWeek.plusDays(7).toString()));
        homeVo.setInterviewCount(interviewList.size());

        return homeVo;
    }
}
