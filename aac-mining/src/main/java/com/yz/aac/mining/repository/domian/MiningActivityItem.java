package com.yz.aac.mining.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("挖矿活动项")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiningActivityItem {

    /*** ID */
    private Long id;

    /*** 挖矿活动ID */
    private Long activityId;

    /*** 限制进入等级ID（NULL为不限制等级） */
    private Long userLevelId;

    /*** 场次总奖金 */
    private BigDecimal totalBonus;

    /*** 暴击率（百分号之前的值） */
    private BigDecimal luckyRate;

    /*** 暴击倍数 */
    private Integer luckyTimes;

    /*** 点击第几次出现广告 */
    private Integer hitAdNumber;

    /*** 排序号 */
    private Integer orderNumber;
}
