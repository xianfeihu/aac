package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProperty {

    private Long id;

    private Long userId;

    private Integer powerPoint;

    private Long increaseStrategyId;

    private Integer status;

    private String statusDescription;

    public UserProperty() {

    }

}
