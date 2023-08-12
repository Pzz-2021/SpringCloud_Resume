package com.resume.dubbo.api;

import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDTO;
import com.resume.dubbo.domian.Resume;
import com.resume.dubbo.domian.SearchCondition;

import java.util.List;

public interface SearchService {
    /*
        职位
     */
    // 全量保存
    Boolean savePositionDTOs(List<PositionDTO> positionDTOList);

    // 增量保存
    Boolean savePositionDTO(PositionDTO positionDTO);

    // 根据 id 删除一个职位信息
    Boolean deletePositionDTOById(Long id);

    // 根据 id 更新一个职位信息
    Boolean updatePositionDTOById(PositionDTO positionDTO);

    // 根据 id 查找一个职位信息
    Position getPositionById(Long id);

    // 分页搜索职位信息
    PageBean<Position> searchPosition(SearchCondition searchCondition, TokenInfo tokenInfo);


    /*
        简历
     */
    // 全量保存
    Boolean saveResumes(List<Resume> resumeList);

    // 增量保存
    Boolean saveResume(Resume resume);

    // 根据 id 更新一个简历信息
    Boolean updateResumeById(Resume resume);

    // 根据 id 查找一个简历信息
    Resume getResumeById(Long id);

    // 分页搜索简历信息
    PageBean<Resume> searchResume(SearchCondition searchCondition, TokenInfo tokenInfo);


}
