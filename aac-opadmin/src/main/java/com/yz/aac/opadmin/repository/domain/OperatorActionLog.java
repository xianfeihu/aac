package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperatorActionLog {

    private Long id;

    private Long operatorId;

    private Integer module;

    private Integer action;

    private String additionalInfo;

    private Long actionTime;

    private Long beginActionTime;

    private Long endActionTime;

    public OperatorActionLog() {
    }
}
