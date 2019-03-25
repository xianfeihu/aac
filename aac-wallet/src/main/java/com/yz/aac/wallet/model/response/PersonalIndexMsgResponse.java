package com.yz.aac.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("我的-首页信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalIndexMsgResponse {

    @ApiModelProperty(value = "用户ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "姓名", position = 2)
    private String name;

    @ApiModelProperty(value = "元力值", position = 3)
    private Integer powerPoint;

    @ApiModelProperty(value = "平台币", position = 4)
    private BigDecimal platCoin;

    @ApiModelProperty(value = "级别", position = 5)
    private String level;

    @ApiModelProperty(value = "交易次数", position = 6)
    private Long transactionsCount;

    @ApiModelProperty(value = "转账次数", position = 7)
    private Integer transferCount;

    @ApiModelProperty(value = "收款次数", position = 8)
    private Integer receiptCount;

    public PersonalIndexMsgResponse(Integer id, String name, Integer powerPoint, BigDecimal platCoin, Long transactionsCount) {
        this.id = id.longValue();
        this.name = name;
        this.powerPoint = powerPoint;
        this.platCoin = platCoin;
        this.transactionsCount = transactionsCount;
    }
}
