package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询账号响应")
public class QueryAccountResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<Item> items;

    @ApiModel("查询账号响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "ID（编号）", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "账号", position = 2, required = true)
        private String loginName;

        @ApiModelProperty(value = "姓名", position = 3, required = true)
        private String name;

        @ApiModelProperty(value = "部门", position = 4, required = true)
        private String department;

        @ApiModelProperty(value = "角色（1-普通运营；2-超级管理员）", position = 5, required = true)
        private Integer role;

        @ApiModelProperty(value = "状态（1-启用；2-停用）", position = 6, required = true)
        private int status;
    }

}
