package com.resume.position.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.DateUtil;
import com.resume.base.utils.JwtUtil;
import com.resume.position.pojo.Position;
import com.resume.position.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "职位接口")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @ApiOperation("添加职位")
    @PostMapping("/add-position")
    public RestResponse<String> addPosition(HttpServletRequest httpServletRequest, @RequestBody Position position) {
        TokenInfo tokenInfo=JwtUtil.getTokenInfo(httpServletRequest);
        position.setCreateUserId(tokenInfo.getPkUserId());
        position.setCompanyId(tokenInfo.getCompanyId());
        position.setState(1);//在招
        position.setCreateTime(DateUtil.getDate2());
        boolean save = positionService.save(position);
        return RestResponse.judge(save);
    }

    public RestResponse<String> selectPositionByPage(HttpServletRequest httpServletRequest,@RequestParam(defaultValue = "1")int nowPage) {
         TokenInfo tokenInfo=JwtUtil.getTokenInfo(httpServletRequest);
         positionService.selectPositionByPage(tokenInfo);
         return null;
    }


//    @GetMapping("/{positionId}")
//    public RestResponse getOne(HttpServletRequest httpServletRequest, @PathVariable Long positionId) {
////        Long companyId = JwtUtil.getCompanyId(httpServletRequest);
//        Long companyId = 1L;
//        Position position = positionService.getOne(companyId, positionId);
//        if (position == null)
//            return RestResponse.error("查询失败。");
//        return RestResponse.success(position);
//    }
}

