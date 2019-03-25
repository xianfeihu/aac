package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryUserFreezingAssertRequest {

    private Long userId;

    private PagingRequest paging;
}
