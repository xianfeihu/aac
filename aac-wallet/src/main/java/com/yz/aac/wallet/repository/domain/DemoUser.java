package com.yz.aac.wallet.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DemoUser {

    private Long id;

    private String name;

    private Integer age;
}
