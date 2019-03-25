package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryDashboardRecordRequest {

    private Integer userRole;

    private Integer tradeType;

    private Long beginTime;

    private Long endTime;

    private String userName;

    private String userCode;

    private Boolean outputStatistics;

    private String currency;

    private PagingRequest paging;

}
