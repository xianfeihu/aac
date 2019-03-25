package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PlatformServiceChargeStrategy {

    private Long id;

    private String name;

    private String accurateName;

    private Double tradeChargeRate;

    private BigDecimal issuanceDeposit;

    private Integer isDefault;

    private Long createTime;

    private Long updateTime;

    public PlatformServiceChargeStrategy() {
    }
}
