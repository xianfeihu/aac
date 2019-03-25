package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询首页交易记录响应")
public class QueryDashboardRecordResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "平台币符号", position = 2, required = true)
    private String platformCurrencySymbol;

    @ApiModelProperty(value = "交易总量", position = 3)
    private BigDecimal totalTradeAmount;

    @ApiModelProperty(value = "分页数据明细", position = 4, required = true)
    private List<QueryDashboardRecordResponse.Item> items;

    @ApiModel("查询首页交易记录响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户编号", position = 1, required = true)
        private String userCode;

        @ApiModelProperty(value = "用户姓名", position = 2, required = true)
        private String userName;

        @ApiModelProperty(value = "钱包地址", position = 3, required = true)
        private String walletAddress;

        @ApiModelProperty(value = "可用余额", position = 4, required = true)
        private BigDecimal availableBalance;

        @ApiModelProperty(value = "交易时间", position = 5, required = true)
        private Long tradeTime;

        @ApiModelProperty(value = "交易数量", position = 6, required = true)
        private BigDecimal tradeAmount;

        @ApiModelProperty(value = "挂单人或购买方姓名", position = 7)
        private String partnerName;

        @ApiModelProperty(value = "消耗流向", position = 8)
        private String tradeResult;

    }

}
