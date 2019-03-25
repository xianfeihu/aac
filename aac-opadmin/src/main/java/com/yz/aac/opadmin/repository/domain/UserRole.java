package com.yz.aac.opadmin.repository.domain;

import lombok.Data;

import java.util.Set;

@Data
public class UserRole {

    private Long id;

    private Long userId;

    private Set<Long> userIds;

    private Integer isMerchant;

    private Integer isAdvertiser;

}
