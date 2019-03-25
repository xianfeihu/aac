package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryExchangeRequest {

    private Long id;

    private Boolean includeItem;

}
