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
@ApiModel("抢到红包请求响应数据")
public class PacketReceiveInfoResponse {

	@ApiModelProperty(value = "收到总金额", position = 1)
    private BigDecimal totalAmount;
	
    @ApiModelProperty(value = "最佳手气", position = 2)
    private BigDecimal bestLuckAmount;
    
    @ApiModelProperty(value = "领取记录", position = 3)
    private PageResult<PacketHistoryResponse> recordList;
    
}
