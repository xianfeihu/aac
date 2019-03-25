package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryPlatformRecordRequest {

    private Long beginTime;

    private Long endTime;

    private String userName;

    private Long id;

    private String payNumber;

    private PagingRequest paging;
}
