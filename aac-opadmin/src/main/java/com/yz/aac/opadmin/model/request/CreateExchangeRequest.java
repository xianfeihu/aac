package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("创建或更新兑换服务请求")
public class CreateExchangeRequest {

    @ApiModelProperty(value = "服务ID", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "是否支持自定义金额（1-是；2-否）", position = 2, required = true)
    private Integer customized;

    @ApiModelProperty(value = "每月可充值次数", position = 3, required = true)
    private Integer limitInMonth;

    @ApiModelProperty(value = "服务项", position = 4, required = true)
    private List<CreateExchangeRequest.Item> items;

    @ApiModel("创建或更新兑换服务-服务项")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "充值法币金额", position = 1, required = true)
        private Integer rmbAmount;

        @ApiModelProperty(value = "支付平台币金额", position = 2, required = true)
        private Integer platformAmount;
    }

}
