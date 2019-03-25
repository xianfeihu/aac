package com.yz.aac.exchange.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyMallIndexInfoForTaskResponse {
    /** 查询的type */
    private Integer type;
    /**  插入的type */
    private Integer countType;
    /** 开始时间 */
    private Long beginTime;
    /** 结束时间 */
    private Long endTime;
    /** 创建时间 */
    private Long createTime;
}
