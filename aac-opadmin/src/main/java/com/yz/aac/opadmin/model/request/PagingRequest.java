package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingRequest {

    private int pageNumber;

    private int pageSize;

}