package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("其他资产详情")
public class OtherCoinMsgResponse {

    @ApiModelProperty(value = "其它资产数据页-总资产=(AAC)", position = 1, required = true)
    private BigDecimal otherAssetsSum;
    @ApiModelProperty(value = "其它资产数据页-列表", position = 2, required = true)
    private List<CoinDetailsMsgResponse> coinDetailsMsg;

}
