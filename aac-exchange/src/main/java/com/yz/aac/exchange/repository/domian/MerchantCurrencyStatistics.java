package com.yz.aac.exchange.repository.domian;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@ApiModel("冻结卖单明细")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantCurrencyStatistics {
    private Long merchantId;
    private Integer assetType;
    private BigDecimal currencyNum;
    private Integer countType;
    private Long createTime;


}
