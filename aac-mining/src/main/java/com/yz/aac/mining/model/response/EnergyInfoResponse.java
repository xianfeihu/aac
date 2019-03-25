package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("获取推荐能量详情响应对象")
public class EnergyInfoResponse {

	@ApiModelProperty(value = "邀请-已邀请好友数", position = 1)
    private Integer inviteAddNum;
	
	@ApiModelProperty(value = "邀请-每天最多邀请数", position = 2)
    private Integer inviteMaxEveryday;
    
    @ApiModelProperty(value = "邀请-邀请奖励/位", position = 3)
    private Integer inviteReward;
    
    @ApiModelProperty(value = "平台币符号", position = 4)
    private String platformCurrency;
    
	
}
