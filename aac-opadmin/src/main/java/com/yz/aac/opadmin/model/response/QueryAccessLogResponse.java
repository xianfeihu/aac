package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询访问日志响应")
public class QueryAccessLogResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryAccessLogResponse.Item> items;

    @ApiModel("查询访问日志响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "访问时间", position = 1, required = true)
        private Long actionTime;

        @ApiModelProperty(value = "功能模块", position = 2, required = true)
        private String module;

        @ApiModelProperty(value = "操作描述", position = 3, required = true)
        private String action;

        @ApiModelProperty(value = "附加信息", position = 4, required = true)
        private String additionalInfo;
    }

}


