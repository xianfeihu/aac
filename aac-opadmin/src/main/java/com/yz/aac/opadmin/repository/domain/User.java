package com.yz.aac.opadmin.repository.domain;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String name;

    private Integer gender;

    private String idNumber;

    private Long mobileNumber;

    private String paymentPassword;

    private Integer source;

    private Long regTime;

    private Long inviterId;

    private String inviterCode;

}
