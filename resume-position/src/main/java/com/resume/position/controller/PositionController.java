package com.resume.position.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.utils.DateUtil;
import com.resume.base.utils.JwtUtil;
import com.resume.position.pojo.Position;
import com.resume.position.service.IPositionService;
import com.resume.position.service.impl.PositionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 彭政
 * @since 2023-06-18
 */
@RestController
@RequestMapping("/position")
public class PositionController {
    @Autowired
    private IPositionService positionService;

    @PostMapping
    public RestResponse addOne(HttpServletRequest httpServletRequest, @RequestBody Position position) {
        Long companyId = JwtUtil.getCompanyId(httpServletRequest);

        position.setCompanyId(companyId);
        position.setCreateTime(DateUtil.getDate2());

        boolean save = positionService.save(position);

        return RestResponse.judge(save);
    }

    @GetMapping("/{positionId}")
    public RestResponse getOne(HttpServletRequest httpServletRequest, @PathVariable Long positionId) {
//        Long companyId = JwtUtil.getCompanyId(httpServletRequest);
        Long companyId = 1L;

        Position position = positionService.getOne(companyId, positionId);

        if (position == null)
            return RestResponse.error("查询失败。");
        return RestResponse.success(position);
    }

}

