package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParamConfig {

    private Long id;

    private Integer category;

    private Integer subCategory;

    private String key;

    private String value;

    public ParamConfig() {

    }

}
