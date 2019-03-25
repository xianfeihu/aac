package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询平台币卖单响应")
public class QueryPlatformOrderResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryPlatformOrderResponse.Item> items;

    @ApiModel("查询平台币卖单响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "卖单编号", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "挂单人ID", position = 2, required = true)
        private Long sellerId;

        @ApiModelProperty(value = "挂单人名称", position = 3, required = true)
        private String sellerName;

        @ApiModelProperty(value = "数量", position = 4, required = true)
        private BigDecimal availableTradeAmount;

        @ApiModelProperty(value = "平台币单位", position = 5, required = true)
        private String platformPriceUnit;

        @ApiModelProperty(value = "最小限购额", position = 6, required = true)
        private BigDecimal minAmountLimit;

        @ApiModelProperty(value = "最大限购额", position = 7, required = true)
        private BigDecimal maxAmountLimit;

        @ApiModelProperty(value = "最小法币限购额", position = 8, required = true)
        private BigDecimal minRmbAmountLimit;

        @ApiModelProperty(value = "最大法币限购额", position = 9, required = true)
        private BigDecimal maxRmbAmountLimit;

        @ApiModelProperty(value = "限价", position = 10, required = true)
        private BigDecimal rmbPrice;

        @ApiModelProperty(value = "备注", position = 11, required = true)
        private String remark;
    }


}
