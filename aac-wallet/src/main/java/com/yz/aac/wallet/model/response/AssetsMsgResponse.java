package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@ApiModel("我的币资产数据响应")
public class AssetsMsgResponse {

    @ApiModelProperty(value = "我的基础信息", position = 1, required = true)
    private Object personalBaseMsg;

    @ApiModelProperty(value = "转账记录", position = 2, required = true)
    private Object personalTransferRecord;

}
