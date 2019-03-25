package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PlatformAssertSeller {

    private Long id;

    private String name;

    private String accurateName;

    private Integer supportAlipay;

    private Integer supportWechat;

    private Integer supportBankCard;

    private String alipayAccount;

    private String alipayQrCodePath;

    private String wechatAccount;

    private String wechatQrCodePath;

    private String bankCardNumber;

    private BigDecimal totalSoldCurrency;

    private Integer totalSoldCount;

    private Long createTime;

    private Long updateTime;

    public PlatformAssertSeller() {

    }
}
