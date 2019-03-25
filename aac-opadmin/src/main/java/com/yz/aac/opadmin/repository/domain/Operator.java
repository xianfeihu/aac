package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Operator {

    private Long id;

    private String loginName;

    private String password;

    private String name;

    private String department;

    private Integer status;

    private Integer role;

    private Long createTime;

    private Long updateTime;

    public Operator() {
    }
}
