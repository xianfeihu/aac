package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    private Long id;

    private String name;

    private String merchantName;

    private Integer gender;

    private String idNumber;

    private Long mobileNumber;

    private Long createTime;

}
