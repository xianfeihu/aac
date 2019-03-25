package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryAlgorithmRequest {

    private String name;

    private PagingRequest paging;
}
