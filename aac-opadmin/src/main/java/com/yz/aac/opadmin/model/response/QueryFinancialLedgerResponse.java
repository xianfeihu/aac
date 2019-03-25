package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@ApiModel("查询财务进出帐响应")
public class QueryFinancialLedgerResponse {

    @ApiModelProperty(value = "平台币进出帐金额统计", position = 1)
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "法币进出帐金额统计", position = 2)
    private BigDecimal totalRmbAmount;

    @ApiModelProperty(value = "总数据量", position = 3, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 4, required = true)
    private List<QueryFinancialLedgerResponse.Item> items;

    @ApiModel("查询财务进出帐响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {

        @ApiModelProperty(value = "ID（编号）", position = 1, required = true)
        private Long id;

        @ApiModelProperty(hidden = true)
        private Long userId;

        @ApiModelProperty(value = "用户分类，一个用户存在多种角色（2-普通用户；3-商户；4-广告主）", position = 2, required = true)
        private Set<Integer> userRoles;

        @ApiModelProperty(value = "用户姓名", position = 3, required = true)
        private String userName;

        @ApiModelProperty(value = "进出帐时间", position = 4, required = true)
        private Long actionTime;

        @ApiModelProperty(value = "平台币进出帐金额", position = 5, required = true)
        private BigDecimal actionCurrency;

        @ApiModelProperty(value = "法币进出帐金额", position = 6)
        private BigDecimal rmbAmount;

        @ApiModelProperty(value = "财务明细（1-购买平台币；2-发币押金；3-交易手续费；4-广告费；5-购买平台币转账；6-兑换话费；7-兑换中石油油卡；8-兑换中石化油卡；9-充值话费；10-充值中石油油卡；11-充值中石化油卡）", position = 7, required = true)
        private Integer action;
    }

}
