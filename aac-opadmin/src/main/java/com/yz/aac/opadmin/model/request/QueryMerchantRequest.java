package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryMerchantRequest {

    private String currencySymbol;

    private Long beginTime;

    private Long endTime;

    private Integer status;

    private String merchantName;

    private Long mobileNumber;

    private String merchantCode;

    private Boolean outputStatistics;

    private PagingRequest paging;
}
