package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询用户冻结资产响应")
public class QueryUserFreezingAssertResponse {


    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryUserFreezingAssertResponse.Item> items;

    @ApiModel("查询用户冻结资产响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "币种", position = 1, required = true)
        private String currencySymbol;

        @ApiModelProperty(value = "冻结额度", position = 2, required = true)
        private BigDecimal freezingAmount;

        @ApiModelProperty(value = "冻结原因", position = 3, required = true)
        private String reason;
    }
}
