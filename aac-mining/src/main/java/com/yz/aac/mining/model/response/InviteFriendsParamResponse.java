package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("邀请好友-分享参数")
public class InviteFriendsParamResponse {

	@ApiModelProperty(value = "邀请好友奖励平台币数", position = 1)
	private BigDecimal invitationCurrency;
	
	@ApiModelProperty(value = "每日邀请好友最大数 ", position = 2)
	private Integer maxInvitationPerDay;
	
	@ApiModelProperty(value = "好友挖矿分成比例", position = 3)
	private Integer miningRoyaltyRate;
	
	@ApiModelProperty(value = "平台币符号", position = 4)
	private String platformCurrency;
	
}
