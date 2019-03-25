package com.yz.aac.opadmin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("资产解冻请求")
public class UnfreezeAssetRequest {

    @ApiModelProperty(value = "用户ID", position = 1, required = true)
    private Long userId;

    @ApiModelProperty(value = "冻结类型(1-发币押金)", position = 2, required = true)
    private Integer freezingType;

}
