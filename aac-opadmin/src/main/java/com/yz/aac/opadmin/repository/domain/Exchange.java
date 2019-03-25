package com.yz.aac.opadmin.repository.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    @ApiModelProperty(value = "ID", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2, required = true)
    private String name;

    private Integer category;

    private Integer subCategory;

    @ApiModelProperty(value = "是否支持自定义金额（1-是；2-否）", position = 3, required = true)
    private Integer customized;

    @ApiModelProperty(value = "每月可充值次数", position = 4, required = true)
    private Integer limitInMonth;

    @ApiModelProperty(value = "是否已开通（1-是；2-否）", position = 5, required = true)
    private Integer activated;

    private Integer orderNumber;

    @ApiModelProperty(value = "服务项数据", position = 6, required = true)
    private List<ExchangeItem> items;

}
