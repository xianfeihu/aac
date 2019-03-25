package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class IncreaseStrategy {

    private Long id;

    private String name;

    private String accurateName;

    private Integer increasedPowerPoint;

    private Integer consumedAd;

    private BigDecimal platformCurrency;

    private Integer isDefault;

    private Long createTime;

    private Long updateTime;

    public IncreaseStrategy() {

    }

}
