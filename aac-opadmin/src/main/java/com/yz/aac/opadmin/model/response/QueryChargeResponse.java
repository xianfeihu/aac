package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询收费策略响应")
public class QueryChargeResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryChargeResponse.Item> items;

    @ApiModel("查询收费策略响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "收费策略ID", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "收费策略名称", position = 2, required = true)
        private String name;

        @ApiModelProperty(value = "交易费率", position = 3, required = true)
        private Double tradeChargeRate;

        @ApiModelProperty(value = "发币押金", position = 4, required = true)
        private BigDecimal issuanceDeposit;

        @ApiModelProperty(value = "是否是默认策略（1-是；2-否）", position = 5, required = true)
        private Integer defaultCharge;
    }


}
