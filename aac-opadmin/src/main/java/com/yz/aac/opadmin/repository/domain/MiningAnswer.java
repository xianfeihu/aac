package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiningAnswer {

    private Long id;

    private Long questionId;

    private Set<Long> questionIds;

    private String name;

    private Integer orderNumber;

    private Integer isCorrect;

}
