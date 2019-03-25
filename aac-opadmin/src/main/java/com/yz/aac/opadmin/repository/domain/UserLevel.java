package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserLevel {

    private Long id;

    private String name;

    private String accurateName;

    private String iconPath;

    private BigDecimal matchCondition;

    public UserLevel() {

    }

}
