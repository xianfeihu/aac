package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询发布的红包响应")
public class QueryRedPacketResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryRedPacketResponse.Item> items;

    @ApiModel("查询发布的红包响应分页数据")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "编号", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "发布者", position = 2, required = true)
        private String name;

        @ApiModelProperty(value = "发布时间", position = 3, required = true)
        private Long issuanceTime;

        @ApiModelProperty(value = "发布地点", position = 4, required = true)
        private String location;

        @ApiModelProperty(value = "范围（单位：公里）", position = 5, required = true)
        private Integer radius;

        @ApiModelProperty(value = "红包个数", position = 6, required = true)
        private Integer dividingNumber;

        @ApiModelProperty(value = "总金额", position = 7, required = true)
        private BigDecimal currencyAmount;

        @ApiModelProperty(value = "领取人数", position = 8, required = true)
        private Integer grabberCount;

    }

}
