package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiningActivity {

    private Long id;

    private Set<Long> ids;

    private Long beginTime;

    private Long endTime;

    private Integer status;


}
