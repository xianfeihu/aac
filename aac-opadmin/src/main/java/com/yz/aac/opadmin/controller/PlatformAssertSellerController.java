package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.ImageUtil;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryPlatformAssertSellerResponse;
import com.yz.aac.opadmin.service.PlatformAssertSellerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INVALID_IMAGE;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@SuppressWarnings({"unused", "Duplicates"})
@RestController
@RequestMapping(value = "/platform/sellers")
@Slf4j
@Api(tags = "平台币挂单人员（系统管理-挂单人员设置）")
public class PlatformAssertSellerController extends BaseController {

    @Autowired
    private PlatformAssertSellerService platformAssertSellerService;

    @ApiOperation("查询挂单人员")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryPlatformAssertSellerResponse> querySellers(
            @ApiParam(name = "name", value = "人员名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateSpecialChar(NAME.value(), name);
        QueryPlatformAssertSellerRequest request = new QueryPlatformAssertSellerRequest(name, new PagingRequest(pageNumber, pageSize));
        return buildSuccess(platformAssertSellerService.queryPlatformAssertSellers(request));
    }

    @ApiOperation(value = "创建挂单人员")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RootResponse<?> createSeller(
            @ApiParam(name = "name", value = "姓名", required = true) @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "supportAlipay", value = "是否支持支付宝", required = true) @RequestParam(value = "supportAlipay", required = false) Boolean supportAlipay,
            @ApiParam(name = "supportWechat", value = "是否支持微信", required = true) @RequestParam(value = "supportWechat", required = false) Boolean supportWechat,
            @ApiParam(name = "supportBankCard", value = "是否支持银行卡", required = true) @RequestParam(value = "supportBankCard", required = false) Boolean supportBankCard,
            @ApiParam(name = "alipayAccount", value = "支付宝账号（supportAlipay=true时，必须输入）") @RequestParam(value = "alipayAccount", required = false) String alipayAccount,
            @ApiParam(name = "wechatAccount", value = "微信账号（supportWechat=true时，必须输入）") @RequestParam(value = "wechatAccount", required = false) String wechatAccount,
            @ApiParam(name = "bankCardNumber", value = "银行卡号（supportBankCard=true时，必须输入）") @RequestParam(value = "bankCardNumber", required = false) String bankCardNumber,
            @ApiParam(name = "alipayQrCodeIcon", value = "支付宝账号二维码图片（supportAlipay=true时，必须输入）") @RequestParam(value = "alipayQrCodeIcon", required = false) MultipartFile alipayQrCodeIcon,
            @ApiParam(name = "wechatQrCodeIcon", value = "微信账号二维码图片（supportWechat=true时，必须输入）") @RequestParam(value = "wechatQrCodeIcon", required = false) MultipartFile wechatQrCodeIcon
    ) throws Exception {
        validateRequired(NAME.value(), name);
        validateRequired(SUPPORT_ALIPAY.value(), supportAlipay);
        validateRequired(SUPPORT_WECHAT.value(), supportWechat);
        validateRequired(SUPPORT_BANK_CARD.value(), supportBankCard);
        if (supportAlipay) {
            validateRequired(ALIPAY_ACCOUNT.value(), alipayAccount);
            validateRequired(ALIPAY_QR_CODE_ICON.value(), alipayQrCodeIcon);
            validateStringLength(ALIPAY_ACCOUNT.value(), alipayAccount, 1, 32);
            validateImage(alipayQrCodeIcon.getInputStream(), ALIPAY_QR_CODE_ICON.value());
        }
        if (supportWechat) {
            validateRequired(WECHAT_ACCOUNT.value(), wechatAccount);
            validateRequired(WECHAT_QR_CODE_ICON.value(), wechatQrCodeIcon);
            validateStringLength(WECHAT_ACCOUNT.value(), wechatAccount, 1, 32);
            validateImage(wechatQrCodeIcon.getInputStream(), WECHAT_QR_CODE_ICON.value());
        }
        if (supportBankCard) {
            validateRequired(BANK_CARD_NUMBER.value(), bankCardNumber);
            validateStringLength(BANK_CARD_NUMBER.value(), bankCardNumber, 1, 32);
        }
        validateStringLength(NAME.value(), name, 1, 16);
        validateSpecialChar(NAME.value(), name);
//        validateSpecialChar(ALIPAY_ACCOUNT.value(), alipayAccount);
//        validateSpecialChar(WECHAT_ACCOUNT.value(), wechatAccount);
        validateSpecialChar(BANK_CARD_NUMBER.value(), bankCardNumber);
        CreatePlatformAssertSellerRequest request = new CreatePlatformAssertSellerRequest(
                name,
                supportAlipay,
                supportWechat,
                supportBankCard,
                alipayAccount,
                UploadRequest.createOrNull(alipayQrCodeIcon),
                wechatAccount,
                UploadRequest.createOrNull(wechatQrCodeIcon),
                bankCardNumber,
                getLoginInfo().getId()
        );
        platformAssertSellerService.createPlatformAssertSeller(request);
        return buildSuccess(null);
    }

    @ApiOperation(value = "更新挂单人员")
    @PostMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RootResponse<?> updateSeller(
            @ApiParam(name = "id", value = "挂单人员ID", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(name = "name", value = "姓名", required = true) @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "supportAlipay", value = "是否支持支付宝", required = true) @RequestParam(value = "supportAlipay", required = false) Boolean supportAlipay,
            @ApiParam(name = "supportWechat", value = "是否支持微信", required = true) @RequestParam(value = "supportWechat", required = false) Boolean supportWechat,
            @ApiParam(name = "supportBankCard", value = "是否支持银行卡", required = true) @RequestParam(value = "supportBankCard", required = false) Boolean supportBankCard,
            @ApiParam(name = "alipayAccount", value = "支付宝账号（supportAlipay=true时，必须输入）") @RequestParam(value = "alipayAccount", required = false) String alipayAccount,
            @ApiParam(name = "wechatAccount", value = "微信账号（supportWechat=true时，必须输入）") @RequestParam(value = "wechatAccount", required = false) String wechatAccount,
            @ApiParam(name = "bankCardNumber", value = "银行卡号（supportBankCard=true时，必须输入）") @RequestParam(value = "bankCardNumber", required = false) String bankCardNumber,
            @ApiParam(name = "updateAlipayQrCodeIcon", value = "是否更新支付宝账号二维码图片（supportAlipay=true时，必须输入）") @RequestParam(value = "updateAlipayQrCodeIcon", required = false) Boolean updateAlipayQrCodeIcon,
            @ApiParam(name = "updateWechatQrCodeIcon", value = "是否更新微信账号二维码图片（supportWechat=true时，必须输入）") @RequestParam(value = "updateWechatQrCodeIcon", required = false) Boolean updateWechatQrCodeIcon,
            @ApiParam(name = "alipayQrCodeIcon", value = "支付宝账号二维码图片（supportAlipay=true且updateAlipayQrCodeIcon=true时，必须输入）") @RequestParam(value = "alipayQrCodeIcon", required = false) MultipartFile alipayQrCodeIcon,
            @ApiParam(name = "wechatQrCodeIcon", value = "微信账号二维码图片（supportWechat=true且updateWechatQrCodeIcon=true时，必须输入）") @RequestParam(value = "wechatQrCodeIcon", required = false) MultipartFile wechatQrCodeIcon
    ) throws Exception {
        validateRequired(NAME.value(), name);
        validateRequired(SUPPORT_ALIPAY.value(), supportAlipay);
        validateRequired(SUPPORT_WECHAT.value(), supportWechat);
        validateRequired(SUPPORT_BANK_CARD.value(), supportBankCard);
        if (supportAlipay) {
            validateRequired(ALIPAY_ACCOUNT.value(), alipayAccount);
            validateRequired(UPDATE_ALIPAY_QR_CODE_ICON.value(), updateAlipayQrCodeIcon);
            if (updateAlipayQrCodeIcon) {
                validateRequired(ALIPAY_QR_CODE_ICON.value(), alipayQrCodeIcon);
                validateImage(alipayQrCodeIcon.getInputStream(), ALIPAY_QR_CODE_ICON.value());
            }
            validateStringLength(ALIPAY_ACCOUNT.value(), alipayAccount, 1, 32);
        }
        if (supportWechat) {
            validateRequired(WECHAT_ACCOUNT.value(), wechatAccount);
            validateRequired(UPDATE_WECHAT_QR_CODE_ICON.value(), updateWechatQrCodeIcon);
            if (updateWechatQrCodeIcon) {
                validateRequired(WECHAT_QR_CODE_ICON.value(), wechatQrCodeIcon);
                validateImage(wechatQrCodeIcon.getInputStream(), WECHAT_QR_CODE_ICON.value());
            }
            validateStringLength(WECHAT_ACCOUNT.value(), wechatAccount, 1, 32);
        }
        if (supportBankCard) {
            validateRequired(BANK_CARD_NUMBER.value(), bankCardNumber);
            validateStringLength(BANK_CARD_NUMBER.value(), bankCardNumber, 1, 32);
        }
        validateStringLength(NAME.value(), name, 1, 16);
        validateSpecialChar(NAME.value(), name);
//        validateSpecialChar(ALIPAY_ACCOUNT.value(), alipayAccount);
//        validateSpecialChar(WECHAT_ACCOUNT.value(), wechatAccount);
        validateSpecialChar(BANK_CARD_NUMBER.value(), bankCardNumber);
        UpdatePlatformAssertSellerRequest request = new UpdatePlatformAssertSellerRequest(
                id,
                name,
                supportAlipay,
                supportWechat,
                supportBankCard,
                alipayAccount,
                wechatAccount,
                updateAlipayQrCodeIcon,
                updateWechatQrCodeIcon,
                UploadRequest.createOrNull(alipayQrCodeIcon),
                UploadRequest.createOrNull(wechatQrCodeIcon),
                bankCardNumber,
                getLoginInfo().getId()
        );
        platformAssertSellerService.updatePlatformAssertSeller(request);
        return buildSuccess(null);
    }

    @ApiOperation("删除挂单人员")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RootResponse<?> deleteSeller(
            @ApiParam(name = "id", value = "挂单人员ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        platformAssertSellerService.deletePlatformAssertSeller(new DeletePlatformAssertSellerRequest(id, getLoginInfo().getId()));
        return buildSuccess(null);
    }

    private void validateImage(InputStream is, String fieldName) throws BusinessException {
        if (!ImageUtil.isImage(is)) {
            throw new BusinessException(MSG_INVALID_IMAGE.code(), MSG_INVALID_IMAGE.message().replaceFirst(PLACEHOLDER.value(), fieldName));
        }
    }
}