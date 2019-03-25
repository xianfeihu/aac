package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("红信首页数据")
public class PackeIndexResponse {

	@ApiModelProperty(value = "剩余可抢次数", position = 1)
    private Integer residueDegree;
	
	@ApiModelProperty(value = "红包信息", position = 2)
    private List<PacketMapDateResponse> mapDateList;
    
    @ApiModelProperty(value = "平台币符号", position = 3)
    private String platformCurrency;
    
}
