package com.yz.aac.opadmin.model.response;

import com.yz.aac.opadmin.model.request.CreateMiningActivityRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询矿活动响应")
@AllArgsConstructor
public class QueryMiningActivityResponse extends CreateMiningActivityRequest {

    @ApiModelProperty(value = "状态（1-只能点击[结束]；2-只能点击[开始]）", position = 2, required = true)
    private Integer status;

    public QueryMiningActivityResponse(List<Activity> activities, Integer status) {
        this.setActivities(activities);
        this.status = status;
    }

}
