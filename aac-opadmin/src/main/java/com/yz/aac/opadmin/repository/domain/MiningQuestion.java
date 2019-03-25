package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MiningQuestion {

    private Long id;

    private Integer isSingleChoice;

    private String name;

    private String accurateName;

    private Integer powerPointBonus;

    public MiningQuestion() {

    }

}
