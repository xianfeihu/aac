package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询平台挂单人员响应")
public class QueryPlatformAssertSellerResponse {


    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<QueryPlatformAssertSellerResponse.Item> items;

    @ApiModel("查询平台挂单人员响应分页数据")
    @AllArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "ID", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "姓名", position = 2, required = true)
        private String name;

        @ApiModelProperty(value = "是否支持支付宝（1-是；2-否）", position = 3, required = true)
        private Integer supportAlipay;

        @ApiModelProperty(value = "是否支持微信（1-是；2-否）", position = 4, required = true)
        private Integer supportWechat;

        @ApiModelProperty(value = "是否支持银行卡（1-是；2-否）", position = 5, required = true)
        private Integer supportBankCard;

        @ApiModelProperty(value = "支付宝账号", position = 6, required = true)
        private String alipayAccount;

        @ApiModelProperty(value = "支付宝二维码URL", position = 7, required = true)
        private String alipayQrCodeUrl;

        @ApiModelProperty(value = "微信账号", position = 8, required = true)
        private String wechatAccount;

        @ApiModelProperty(value = "微信二维码URL", position = 9, required = true)
        private String wechatQrCodeUrl;

        @ApiModelProperty(value = "银行卡号", position = 10, required = true)
        private String bankCardNumber;

        @ApiModelProperty(value = "总计出售数量", position = 11, required = true)
        private BigDecimal totalSoldCurrency;

        @ApiModelProperty(value = "总计购买人次", position = 12, required = true)
        private Integer totalSoldCount;
    }
}
