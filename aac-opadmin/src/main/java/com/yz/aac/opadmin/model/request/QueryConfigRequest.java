package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryConfigRequest {

    private Integer category;

    private Integer subCategory;

    private String key;
}
