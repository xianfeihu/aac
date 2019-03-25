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
@ApiModel("查询挖矿活动参与用户响应")
public class QueryMiningActivityParticipatorResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryMiningActivityParticipatorResponse.Item> items;

    @ApiModel("查询挖矿活动参与用户分页数据")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "用户编号", position = 1, required = true)
        private String userCode;

        @ApiModelProperty(value = "用户姓名", position = 2, required = true)
        private String userName;

        @ApiModelProperty(value = "总计参与次数", position = 3, required = true)
        private Long totalCount;

        @ApiModelProperty(value = "广告点击次数", position = 3, required = true)
        private Long adClickedCount;

        @ApiModelProperty(value = "总计挖矿奖励", position = 5, required = true)
        private BigDecimal totalBonus;

    }

}
