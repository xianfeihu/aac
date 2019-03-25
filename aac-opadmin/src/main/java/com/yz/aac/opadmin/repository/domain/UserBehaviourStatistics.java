package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBehaviourStatistics {

    private Long id;

    private Long userId;

    private String key;

    private Integer value;

    public UserBehaviourStatistics() {

    }
}
