package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("挖矿游戏结算申请（结束后5分钟内可结算）")
public class MiningForSettlementAmountRequest {

    private Long itemId;

    private Long userId;

    // 广告点击次数
    private Integer hitAdNumber;

    // 本次挖矿正常获取金额
    private BigDecimal ordinaryAmount;

    // 本次挖矿暴击获取金额
    private BigDecimal extraAmount;
}
