package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询算法响应")
public class QueryAlgorithmResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryAlgorithmResponse.Item> items;

    @ApiModel("查询算法响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "算法ID", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "算法名称", position = 2, required = true)
        private String name;

        @ApiModelProperty(value = "昨日增长元力（基数）", position = 3, required = true)
        private Integer increasedPowerPoint;

        @ApiModelProperty(value = "昨日广告有效点击率或提交信息总数量（基数）", position = 4, required = true)
        private Integer consumedAd;

        @ApiModelProperty(value = "昨日AAC数量（基数）", position = 5, required = true)
        private BigDecimal platformCurrency;

        @ApiModelProperty(value = "是否是默认算法（1-是；2-否）", position = 6, required = true)
        private Integer defaultAlgorithm;
    }

}
