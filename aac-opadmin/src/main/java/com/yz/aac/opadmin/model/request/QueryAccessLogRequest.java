package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryAccessLogRequest {

    private Long operatorId;

    private Long beginTime;

    private Long endTime;

    private PagingRequest paging;
}
