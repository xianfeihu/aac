package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiningActivityItem {

    private Long id;

    private Long activityId;

    private Set<Long> activityIds;

    private Long userLevelId;

    private BigDecimal totalBonus;

    private Integer luckyRate;

    private Integer luckyTimes;

    private Integer hitAdNumber;

    private Integer orderNumber;

}
