package com.yz.aac.exchange.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取K线图请求数据")
public class CandlestickChartMsgRequest {

    @ApiModelProperty(value = "货币符号", position = 1, required = true)
    private String currencySymbol;

    @ApiModelProperty(value = "类型（1、日---2、周---3、月）", position = 2, required = true)
    private Integer type;

    @ApiModelProperty(hidden = true)
    private Integer num;

}
