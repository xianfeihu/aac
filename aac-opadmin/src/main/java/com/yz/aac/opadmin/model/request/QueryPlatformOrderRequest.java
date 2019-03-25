package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryPlatformOrderRequest {

    private Long id;

    private String name;

    private PagingRequest paging;
}
