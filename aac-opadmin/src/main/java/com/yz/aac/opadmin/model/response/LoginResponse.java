package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class LoginResponse {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(hidden = true)
    private Set<Integer> roles;

    @ApiModelProperty(value = "操作员姓名", position = 1, required = true)
    private String name;

}
