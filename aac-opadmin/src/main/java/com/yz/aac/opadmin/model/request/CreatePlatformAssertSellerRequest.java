package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePlatformAssertSellerRequest {

    private String name;

    private Boolean supportAlipay;

    private Boolean supportWechat;

    private Boolean supportBankCard;

    private String alipayAccount;

    private UploadRequest alipayQrCodeIcon;

    private String wechatAccount;

    private UploadRequest wechatQrCodeIcon;

    private String bankCardNumber;

    private Long loginId;
}
