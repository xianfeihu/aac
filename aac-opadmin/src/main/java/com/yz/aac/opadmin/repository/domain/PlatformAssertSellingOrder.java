package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PlatformAssertSellingOrder {

    private Long id;

    private Long sellerId;

    private String sellerName;

    private BigDecimal availableTradeAmount;

    private BigDecimal minAmountLimit;

    private BigDecimal maxAmountLimit;

    private BigDecimal rmbPrice;

    private String remark;

    private Long createTime;

    private Long updateTime;

    public PlatformAssertSellingOrder() {

    }

}
