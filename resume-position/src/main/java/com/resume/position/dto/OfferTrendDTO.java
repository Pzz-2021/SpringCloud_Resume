package com.resume.position.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 *@filename: OfferTrendDTO
 *@author: lyh
 *@date:2023/8/15 19:30
 *@version 1.0
 *@description TODO
 */
@Data
public class OfferTrendDTO implements Serializable {

    private List<String> date;

    private List<Integer>newOfferNumber;
}
