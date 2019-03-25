package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.yz.aac.mining.model.PageResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发布的红包请求响应数据")
public class PacketIssueInfoResponse {

	@ApiModelProperty(value = "发布总金额", position = 1)
    private BigDecimal totalAmount;
	
    @ApiModelProperty(value = "累计领取人数", position = 2)
    private Integer receiveNum;
    
    @ApiModelProperty(value = "发布记录", position = 3)
    private PageResult<PacketHistoryResponse> recordList;
    
}
