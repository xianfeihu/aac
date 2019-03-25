package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryExchangeRecordRequest {
    private Long id;

    private String userName;

    private Long exchangeId;

    private Set<Long> userIds;

    private Set<Long> exchangeIds;

    private Long beginTime;

    private PagingRequest paging;

}
