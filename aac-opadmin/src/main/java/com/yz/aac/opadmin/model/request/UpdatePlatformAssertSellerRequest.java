package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePlatformAssertSellerRequest {

    private Long id;

    private String name;

    private Boolean supportAlipay;

    private Boolean supportWechat;

    private Boolean supportBankCard;

    private String alipayAccount;

    private String wechatAccount;

    private Boolean updateAlipayQrCodeIcon;

    private Boolean updateWechatQrCodeIcon;

    private UploadRequest alipayQrCodeIcon;

    private UploadRequest wechatQrCodeIcon;

    private String bankCardNumber;

    private Long loginId;

}
