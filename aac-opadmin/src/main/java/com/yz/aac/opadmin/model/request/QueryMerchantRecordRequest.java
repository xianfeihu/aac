package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryMerchantRecordRequest {

    private Long beginTime;

    private Long endTime;

    private String currencySymbol;

    private Integer direction;

    private String userName;

    private String userCode;

    private PagingRequest paging;
}
