package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询商家币交易记录响应")
public class QueryMerchantRecordResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryMerchantRecordResponse.Item> items;

    @ApiModel("查询商家币交易记录响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户编号", position = 1, required = true)
        private String userCode;

        @ApiModelProperty(value = "用户名称", position = 2, required = true)
        private String userName;

        @ApiModelProperty(value = "交易类型（1-买入；2-售出）", position = 3, required = true)
        private Integer tradeType;

        @ApiModelProperty(value = "币种", position = 4, required = true)
        private String currencySymbol;

        @ApiModelProperty(value = "币种对应商户名称", position = 5, required = true)
        private String merchantName;

        @ApiModelProperty(value = "交易额度", position = 6, required = true)
        private BigDecimal tradeAmount;

        @ApiModelProperty(value = "单价", position = 7, required = true)
        private BigDecimal platformPrice;

        @ApiModelProperty(value = "单价单位", position = 8, required = true)
        private String platformPriceUnit;

        @ApiModelProperty(value = "交易时间", position = 9, required = true)
        private Long tradeTime;

        @ApiModelProperty(value = "交易人名称", position = 10, required = true)
        private String partnerName;

        @ApiModelProperty(value = "交易人是否是商家（1-是；2-否）", position = 11, required = true)
        private Integer merchantPartner;

    }

}
