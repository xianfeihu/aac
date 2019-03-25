package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class QueryUserRequest {

    private Long levelId;

    private Long beginTime;

    private Long endTime;

    private BigDecimal minBalance;

    private BigDecimal maxBalance;

    private String name;

    private Long mobileNumber;

    private String code;

    private PagingRequest paging;
}
