package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("服务活动详情")
public class ExchangeActivityInfoResponse {

	@ApiModelProperty(value = "服务ID", position = 1)
	private Long exchangeId;

	@ApiModelProperty(value = "小类（1-1充话费 2-1中石油 2-2中石化）", position = 3)
	private Integer subCategory;

	@ApiModelProperty(value = "剩余兑换次数", position = 4)
	private Integer exchangeCount;

	@ApiModelProperty(value = "是否可以自定义金额（1-是 2-否）", position = 5)
	private Integer customized;

	@ApiModelProperty(value = "活动选项集合", position = 6)
	private List<ExchangeActivityItemResponse> itemList;

	public ExchangeActivityInfoResponse(Long exchangeId, Integer subCategory, Integer exchangeCount, Integer customized){
		this.exchangeId = exchangeId;
		this.subCategory = subCategory;
		this.exchangeCount = exchangeCount;
		this.customized = customized;
	}

}
