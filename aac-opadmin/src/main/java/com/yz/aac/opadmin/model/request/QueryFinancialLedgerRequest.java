package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryFinancialLedgerRequest {

    private Integer business;

    private Integer direction;

    private Integer userRole;

    private Long beginTime;

    private Long endTime;

    private String userName;

    private Long id;

    private PagingRequest paging;

    private Boolean outputStatistics;
}
