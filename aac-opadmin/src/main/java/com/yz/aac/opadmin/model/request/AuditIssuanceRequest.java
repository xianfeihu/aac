package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("发币审核请求")
public class AuditIssuanceRequest {

    @ApiModelProperty(hidden = true)
    private Long loginId;

    @ApiModelProperty(hidden = true)
    private Long requestId;

    @ApiModelProperty(value = "审核提交状态（2-发币审核通过；6-发币审核失败；5-押金审核通过；4-押金审核失败）", position = 1, required = true)
    private Integer status;

    @ApiModelProperty(value = "服务费策略ID（status=2时，必须输入）", position = 2)
    private Long serviceChargeId;

    @ApiModelProperty(value = "备注（status=6 或 status=4时，必须输入）", position = 3)
    private String remark;
}
