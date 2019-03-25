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
@ApiModel("挖矿活动统计")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiningActivityStatistics {

    /*** ID */
    private Long id;

    /*** 挖矿活动项ID */
    private Long activityItemId;

    /*** 参与者用户ID */
    private Long userId;

    /*** 获利平台币数 */
    private BigDecimal gained;

    /*** 广告点击数 */
    private Integer adClicked;
}
