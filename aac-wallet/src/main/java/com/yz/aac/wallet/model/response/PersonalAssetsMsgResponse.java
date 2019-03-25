package com.yz.aac.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
@ApiModel("我的币资产数据响应")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalAssetsMsgResponse {

    @ApiModelProperty(value = "币的余额", position = 1, required = true)
    private BigDecimal balance;

    @ApiModelProperty(value = "钱包地址", position = 2, required = true)
    private String walletAddress;

    @ApiModelProperty(value = "换算成其它币种", position = 3, required = true)
    private BigDecimal aboutOtherCoin;

    @ApiModelProperty(value = "转账记录", position = 4, required = true)
    private List<UserTransferRecordResponse> personalTransferRecord;

}
