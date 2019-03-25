package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PlatformAssertIncomeExpenditureRecord {

    private Long id;

    private Integer direction;

    private Integer userRole;

    private Long userId;

    private String userName;

    private Long actionTime;

    private Long beginActionTime;

    private Long endActionTime;

    private BigDecimal amount;

    private BigDecimal rmbAmount;

    private Integer action;

    private BigDecimal sumAmount;

    private BigDecimal sumRmbAmount;

    private Integer business;

    public PlatformAssertIncomeExpenditureRecord() {

    }
}
