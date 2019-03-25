package com.yz.aac.wallet.controller;

import com.yz.aac.common.Constants;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.request.ExchangeRequest;
import com.yz.aac.wallet.model.request.TransferToWalletAddrRequest;
import com.yz.aac.wallet.model.response.*;
import com.yz.aac.wallet.service.PersonalService;
import com.yz.aac.wallet.util.RegularUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_TOTAL_CIRCULATION;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@SuppressWarnings("unused")
@Slf4j
@RestController
@Api(tags = "APP端-AAB资产中心")
@RequestMapping(value = "/assets")
public class AssetsController extends BaseController {

    @Autowired
    private PersonalService personalServiceImpl;

    @GetMapping("/")
    @ResponseBody
    @ApiOperation("指定币类型资产数据页")
    @ApiImplicitParam(paramType="query", name = "coinType", value = "币的类型", required = true, dataType = "String")
    public RootResponse<PersonalAssetsMsgResponse> getAssetsForCoinType(String coinType) throws Exception {
        // 数据校验
        this.validateRequired("币的类型" ,coinType);
        PersonalAssetsMsgResponse personalBaseMsg = this.personalServiceImpl.getPersonalBaseMsg(getUserId(), coinType);
        return buildSuccess(personalBaseMsg);
    }

    @GetMapping("/transactionRecord")
    @ApiOperation("获取用户交易记录")
    @ApiImplicitParam(paramType="query", name = "coinType", value = "币的类型", required = true, dataType = "String")
    public RootResponse<List<TradeRecordResponse>> getTransactionRecord(String coinType) throws Exception {
        // 数据校验
        this.validateRequired("币的类型" ,coinType);
        return buildSuccess(this.personalServiceImpl.getTransactionRecord(getUserId(), coinType));
    }

    @PostMapping("/transferToWalletAddr")
    @ApiOperation("转账")
    public RootResponse<?> transferToWalletAddr(@RequestBody TransferToWalletAddrRequest transferToWalletAddr) throws Exception {
        // 数据校验
        this.validateBigDecimalRange("转账金额" ,transferToWalletAddr.getAmount(), BigDecimal.ZERO, BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_TOTAL_CIRCULATION.value())));
        this.validateRequired("币的类型" ,transferToWalletAddr.getCoinType());
        this.validateIntRange("支付密码" ,transferToWalletAddr.getPayPass() , 1 ,999999);
        this.validateRequired("接收地址" ,transferToWalletAddr.getReceiveAddr());
        this.validateRequired("发送地址" ,transferToWalletAddr.getSendAddr());
        transferToWalletAddr.setUserId(getUserId());

        this.personalServiceImpl.transferToWalletAddr(transferToWalletAddr);
        return buildSuccess(null);
    }

    @GetMapping("/otherAssets")
    @ApiOperation("其它资产数据页")
    public RootResponse<OtherCoinMsgResponse> getOtherAssets() throws Exception {
        return buildSuccess(this.personalServiceImpl.getOtherAssets(getUserId()));
    }

    @GetMapping("/mchQrCode")
    @ApiOperation("获取商家二维码")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    public RootResponse<String> getMchQrCode(String currencySymbol) throws Exception {
        // 数据校验
        this.validateRequired("币的类型" ,currencySymbol);
        return buildSuccess(this.personalServiceImpl.getMchQrCode(currencySymbol));
    }

    @GetMapping("/freezeAssets")
    @ApiOperation("获取冻结资产")
    public RootResponse<List<CoinDetailsMsgResponse>> getFreezeAssets() throws Exception {
        return buildSuccess(this.personalServiceImpl.getFreezeAssets(getUserId()));
    }

    @PostMapping("/exchange")
    @ApiOperation("兑换")
    public RootResponse<String> exchange(@RequestBody ExchangeRequest exchangeRequest) throws Exception {
        // 数据校验
        this.validateRequired("兑换服务ID" ,exchangeRequest.getExchangeId());
        this.validateRequired("兑换类型" ,exchangeRequest.getExchangeType());
        this.validateRequired("充值号码" ,exchangeRequest.getChargingNumber());
        this.validateRequired("支付密码" ,exchangeRequest.getPayPass());
        this.validateRequired("充值类型" ,exchangeRequest.getTagId());

        if (com.yz.aac.wallet.Constants.ExchangeTypeEnum.TELEPHONE_BILL.code() == exchangeRequest.getExchangeType()) {
            if (!RegularUtil.phoneVerification(exchangeRequest.getChargingNumber())) {
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
            }
        } else if (com.yz.aac.wallet.Constants.ExchangeTypeEnum.CNPC.code() == exchangeRequest.getExchangeType()) {
            if (!RegularUtil.cnpcCardVerification(exchangeRequest.getChargingNumber())) {
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "中石油号码格式错误！");
            }
        } else {
            if (!RegularUtil.sinopecCardVerification(exchangeRequest.getChargingNumber())) {
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "中石化号码格式错误！");
            }
        }

        if (com.yz.aac.wallet.Constants.ExchangeRechargeTypeEnum.OPTION.code() != exchangeRequest.getTagId()
                && com.yz.aac.wallet.Constants.ExchangeRechargeTypeEnum.MANUAL.code() != exchangeRequest.getTagId()) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "充值类型错误！");
        }

        if (com.yz.aac.wallet.Constants.ExchangeRechargeTypeEnum.MANUAL.code() == exchangeRequest.getTagId()) {
            this.validateRequired("充值金额" ,exchangeRequest.getRmbAmount());
            this.validateIntRange("充值金额" ,exchangeRequest.getRmbAmount(), 40, 10000);
            if (exchangeRequest.getRmbAmount()%10 != 0) {
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "充值金额必须是10的倍数！");
            }
        }

        return buildSuccess(this.personalServiceImpl.exchange(exchangeRequest, getUserId()));
    }

    @GetMapping("/activityList")
    @ApiOperation("兑换服务列表")
    public RootResponse<Set<ActivityListResponse>> activityList() throws Exception {

        return buildSuccess(this.personalServiceImpl.activityList(getUserId()));
    }

    @GetMapping("/serviceInfo")
    @ApiOperation("兑换服务详情")
    @ApiImplicitParam(paramType="query", name = "category", value = "服务类型1-话费 2-油卡", required = true, dataType = "Integer")
    public RootResponse<ExchangeServiceInfoResponse> serviceInfo(Integer category) throws Exception {
        // 数据校验
        this.validateRequired("服务类型" ,category);

        if (null == com.yz.aac.wallet.Constants.ExchangeMaxCategoryEnum.getKey(category)) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "服务类型参数错误！");
        }

        return buildSuccess(this.personalServiceImpl.serviceInfo(category, getUserId()));
    }

    private Long getUserId() {
//        return 16l;
        return this.getLoginInfo().getId();
    }

}