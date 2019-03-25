package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("查询用户兑换记录响应分页数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryExchangeRecordResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryExchangeRecordResponse.Item> items;

    @ApiModel("查询用户兑换记录响应分页数据")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "编号", position = 1, required = true)
        private Long id;

        private Long userId;

        @ApiModelProperty(value = "用户姓名", position = 2, required = true)
        private String userName;

        private Long exchangeId;

        @ApiModelProperty(value = "兑换类型", position = 3, required = true)
        private String serviceName;

        @ApiModelProperty(value = "充值号码", position = 4, required = true)
        private String chargingNumber;

        @ApiModelProperty(value = "充值法币金额", position = 5, required = true)
        private Integer rmbAmount;

        @ApiModelProperty(value = "支付平台币金额", position = 6, required = true)
        private BigDecimal platformAmount;

        @ApiModelProperty(value = "兑换时间", position = 7, required = true)
        private Long recordTime;

        @ApiModelProperty(value = "本月已兑换次数", position = 8, required = true)
        private Integer exchangedTimesInMonth;

        @ApiModelProperty(value = "本月剩余兑换次数", position = 9, required = true)
        private Integer restTimesInMonth;

        @ApiModelProperty(value = "状态（1-待充值；2-充值完成）", position = 10, required = true)
        private Integer status;
    }

}
