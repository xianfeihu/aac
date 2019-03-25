package com.yz.aac.mining.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("挖矿活动")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiningActivity {

    /*** ID*/
    private Long id;

    /*** 开始时间*/
    private Long beginTime;

    /*** 结束时间*/
    private Long endTime;

    /*** 状态 1: 正常 2: 已终止 */
    private Integer status;
}
