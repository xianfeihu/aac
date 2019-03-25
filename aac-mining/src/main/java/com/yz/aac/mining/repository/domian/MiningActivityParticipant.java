package com.yz.aac.mining.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("挖矿活动参与者")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiningActivityParticipant {

    /*** 挖矿活动项ID */
    private Long activityItemId;

    /*** 参与者用户ID */
    private Long userId;

    /*** 参与者排队码 */
    private Integer queuingCode;
}
