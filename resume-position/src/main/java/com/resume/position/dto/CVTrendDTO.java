package com.resume.position.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 *@filename: HireTrendDTO
 *@author: lyh
 *@date:2023/8/15 16:59
 *@version 1.0
 *@description TODO
 */
@Data
public class CVTrendDTO implements Serializable {

    private List<String>date;

    private List<Integer>newCVNumber;

}
