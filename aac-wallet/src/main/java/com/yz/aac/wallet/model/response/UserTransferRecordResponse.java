package com.yz.aac.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.yz.aac.wallet.Constants.PlatformAssertTradeType.DIVIDEND;

@Setter
@ApiModel("商户币交易记录")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTransferRecordResponse {

    /**
     * 是否为平台币交易记录
     */
    @JsonIgnore
    private boolean flag = false;

    @JsonIgnore
    private Long userId;

    @JsonIgnore
    @ApiModelProperty(value = "发起人ID", position = 1, hidden = true)
    private Integer initiatorId;

    @JsonIgnore
    @ApiModelProperty(value = "发起人名字", position = 2, hidden = true)
    private String initiatorName;

    @JsonIgnore
    @ApiModelProperty(value = "交易伙伴ID", position = 3, hidden = true)
    private Integer partnerId;

    @JsonIgnore
    @ApiModelProperty(value = "交易伙伴名字", position = 4, hidden = true)
    private String partnerName;

    @JsonIgnore
    @ApiModelProperty(value = "交易类型", position = 5, hidden = true)
    private Long tradeType;

    @JsonIgnore
    @ApiModelProperty(value = "交易货币符号", position = 6, hidden = true)
    private String currencySymbol;

    @ApiModelProperty(value = "交易时间", position = 7)
    private Long tradeTime;

    @ApiModelProperty(value = "交易金额", position = 8)
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "true-转 false-收", position = 9)
    private Boolean transferToUser;

    @ApiModelProperty(value = "文字描述", position = 10)
    private String message;

    /**
     * 查询转账记录（商户币）
     */
    public UserTransferRecordResponse(Long userId, Integer initiatorId, String initiatorName,Integer partnerId, String partnerName, Long tradeType, Long tradeTime, BigDecimal tradeAmount, String currencySymbol) {
        this.userId = userId;
        this.initiatorId = initiatorId;
        this.initiatorName = initiatorName;
        this.partnerId = partnerId;
        this.tradeTime = tradeTime;
        this.tradeAmount = tradeAmount;
        this.partnerName = partnerName;
        this.currencySymbol = currencySymbol;
        this.tradeType = tradeType;

        if (initiatorId.longValue() == userId) {
            transferToUser = true;
            message = "转账给"+partnerName;
        } else {
            transferToUser = false;
            message = "来自"+initiatorName+"的转账";
        }
    }

    /**
     * 查询转账记录（平台币）
     */
    public UserTransferRecordResponse(Long flag, Long userId, Integer initiatorId, String initiatorName,Integer partnerId, String partnerName, Long tradeType, Long tradeTime, BigDecimal tradeAmount, String currencySymbol) {
        this.flag = flag==1 ? true : false;
        this.userId = userId;
        this.initiatorId = initiatorId;
        this.initiatorName = initiatorName;
        this.partnerId = partnerId;
        this.tradeTime = tradeTime;
        this.tradeAmount = tradeAmount;
        this.partnerName = partnerName;
        this.currencySymbol = currencySymbol;
        this.tradeType = tradeType;

        if (initiatorId.longValue() == userId) {
            transferToUser = true;
            message = "转账给"+partnerName;
        } else {
            transferToUser = false;
            message = "来自"+initiatorName+"的转账";
        }
        if (this.flag && tradeType.intValue()==DIVIDEND.code()) {
            // 平台币转账记录处理信息
            message += "("+ currencySymbol +"分红)";
        }
    }

    public Long getTradeTime() {
        return tradeTime;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public Boolean getTransferToUser() {
        return transferToUser;
    }

    public String getMessage() {
        return message;
    }
}
