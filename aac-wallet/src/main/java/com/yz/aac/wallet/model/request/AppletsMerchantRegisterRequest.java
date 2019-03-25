package com.yz.aac.wallet.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;


@ApiModel("小程序端商户注册接收数据")
@Data
public class AppletsMerchantRegisterRequest {

    private String name;

    private String idNumber;

    private Integer gender;

    private String currencySymbol;

    private String mobileNumber;

    private String code;

    private BigDecimal total;

    private BigDecimal miningRate;

    private BigDecimal fixedIncomeRate;

    private BigDecimal stoDividendRate;

    private Integer incomePeriod;

    private Integer restrictionPeriod;

    private String introduction;

    private String whitePaperUrl;
}
