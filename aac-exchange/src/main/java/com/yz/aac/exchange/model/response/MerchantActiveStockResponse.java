package com.yz.aac.exchange.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yz.aac.exchange.repository.domian.MerchantTradeRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("账户活跃存量返回数据")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantActiveStockResponse {

    @ApiModelProperty(value = "货币总和--flag=true时返回", position = 1)
    private BigDecimal currencySum;

    @ApiModelProperty(value = "该明细数据量总和", position = 2)
    private Long sum;

    @ApiModelProperty(value = "明细", position = 3)
    private List<MerchantTradeRecord> records;

}
