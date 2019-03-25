package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询用户等级响应")
public class QueryUserLevelResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryUserLevelResponse.Item> items;

    @ApiModel("查询用户等级响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "等级ID", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "等级名称", position = 2, required = true)
        private String name;

        @ApiModelProperty(value = "图标URL", position = 3, required = true)
        private String iconUrl;

        @ApiModelProperty(value = "满足该等级所须持有的平台币数量", position = 4, required = true)
        private BigDecimal matchCondition;
    }

}
