package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryMerchantIssuanceRequest {

    private String currencySymbol;

    private Integer status;

    private String merchantName;

    private Long mobileNumber;

    private String merchantCode;

    private PagingRequest paging;
}
