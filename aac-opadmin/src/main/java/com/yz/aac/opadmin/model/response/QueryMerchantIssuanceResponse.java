package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询商户发币申请响应")
public class QueryMerchantIssuanceResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "平台币单位", position = 2, required = true)
    private String currencyUnit;

    @ApiModelProperty(value = "分页数据明细", position = 3, required = true)
    private List<QueryMerchantIssuanceResponse.Item> items;

    @ApiModel("查询商户发币申请响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "商户编号", position = 1, required = true)
        private String merchantCode;

        @ApiModelProperty(value = "状态", position = 2, required = true)
        private Integer status;

        @ApiModelProperty(value = "币种", position = 3, required = true)
        private String currencySymbol;

        @ApiModelProperty(value = "商户名称", position = 4, required = true)
        private String merchantName;

        @ApiModelProperty(value = "可用余额", position = 5, required = true)
        private BigDecimal availableBalance;

        @ApiModelProperty(value = "申请时间", position = 6, required = true)
        private Long requestTime;

        @ApiModelProperty(value = "发行总量", position = 7, required = true)
        private BigDecimal totalAmount;

        @ApiModelProperty(value = "出售占有量", position = 8, required = true)
        private BigDecimal sellingAmount;

        @ApiModelProperty(value = "挖矿占有量", position = 9, required = true)
        private BigDecimal miningAmount;

        @ApiModelProperty(value = "固定收益", position = 10, required = true)
        private BigDecimal fixedIncomeAmount;

        @ApiModelProperty(value = "STO分红", position = 11, required = true)
        private BigDecimal stoDividendAmount;

        @ApiModelProperty(value = "收益周期（单位：天）", position = 12, required = true)
        private Integer incomePeriod;

        @ApiModelProperty(value = "投资限售期（单位：天）", position = 13, required = true)
        private Integer restrictionPeriod;

        @ApiModelProperty(value = "白皮书URL", position = 14, required = true)
        private String whitePaperUrl;

        @ApiModelProperty(value = "审核批注", position = 15, required = true)
        private String auditComment;

        @ApiModelProperty(value = "申请ID", position = 16, required = true)
        private Long requestId;
    }

}
