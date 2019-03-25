package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询平台币交易记录响应")
public class QueryPlatformRecordResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryPlatformRecordResponse.Item> items;

    @ApiModel("查询平台币交易记录响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "交易编号", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "挂单人ID", position = 2, required = true)
        private Long partnerId;

        @ApiModelProperty(value = "挂单人名称", position = 3, required = true)
        private String partnerName;

        @ApiModelProperty(value = "出售数量", position = 4, required = true)
        private BigDecimal availableTradeAmount;

        @ApiModelProperty(value = "平台币单位", position = 5, required = true)
        private String platformPriceUnit;

        @ApiModelProperty(value = "最小限额", position = 6, required = true)
        private BigDecimal minAmountLimit;

        @ApiModelProperty(value = "最大限额", position = 7, required = true)
        private BigDecimal maxAmountLimit;

        @ApiModelProperty(value = "最小法币限额", position = 8, required = true)
        private BigDecimal minRmbAmountLimit;

        @ApiModelProperty(value = "最大法币限额", position = 9, required = true)
        private BigDecimal maxRmbAmountLimit;

        @ApiModelProperty(value = "法币单价", position = 10, required = true)
        private BigDecimal rmbPrice;

        @ApiModelProperty(value = "交易时间", position = 11, required = true)
        private Long tradeTime;

        @ApiModelProperty(value = "购买人", position = 12, required = true)
        private String initiatorName;

        @ApiModelProperty(value = "付款参考号", position = 13, required = true)
        private String payNumber;

        @ApiModelProperty(value = "平台币钱包地址", position = 14, required = true)
        private String walletAddress;

        @ApiModelProperty(value = "购买数量", position = 15, required = true)
        private BigDecimal tradeAmount;

        @ApiModelProperty(value = "状态（1-待转账；2-已完成转账；3-已取消）", position = 16, required = true)
        private Integer status;
    }

}
