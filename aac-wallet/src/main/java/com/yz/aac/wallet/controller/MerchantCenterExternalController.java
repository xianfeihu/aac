package com.yz.aac.wallet.controller;

import com.yz.aac.common.Constants;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.request.MerchantAssertIssuanceMessageRequest;
import com.yz.aac.wallet.model.request.MerchantRegisterBaseMessageRequest;
import com.yz.aac.wallet.model.response.MerchantAssetDetailsResponse;
import com.yz.aac.wallet.model.response.MerchantIssueCurrencyResponse;
import com.yz.aac.wallet.repository.domain.MerchantAssertIssuanceAudit;
import com.yz.aac.wallet.service.MerchantCenterService;
import com.yz.aac.wallet.util.RegularUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.wallet.Constants.MerchantGenderType;
import static com.yz.aac.wallet.Constants.StateType;

@Api(tags = "商户中心后台--对外接口")
@RequestMapping("/external/merchantCenter")
@RestController
public class MerchantCenterExternalController extends BaseController {

    @Autowired
    private MerchantCenterService merchantCenterServiceImpl;

    @ApiOperation("通过手机号校验返回商家ID--（该手机号已被实名注册过，就直接注册商户并返回商户ID）")
    @PostMapping("/checkMerchantByMobile")
    @ApiImplicitParam(paramType="query", name = "mobile", value = "手机号", required = true, dataType = "String")
    public RootResponse<Long> checkMerchantByMobile(String mobile) throws Exception {
        if (!RegularUtil.phoneVerification(mobile)) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
        }
        return buildSuccess(this.merchantCenterServiceImpl.checkMerchantByMobile(mobile));
    }

    @ApiOperation("注册商户-成功后返回商户ID（审核通过后调用，仅可修改商户名和商户二维码）")
    @PostMapping("/merchantBaseMessage")
    public RootResponse<Long> merchantBaseMessage(@RequestBody MerchantRegisterBaseMessageRequest registerRequest) throws Exception {
        if (registerRequest.getMerchantId()==null) {
            this.validateRequired("姓名" ,registerRequest.getName());
            this.validateStringLength("身份证号码" ,registerRequest.getIdNumber() ,15 ,18);
        }
        this.validateRequired("商户名称", registerRequest.getMerchantName());
        this.validateRequired("商家二维码连接", registerRequest.getMerchantVisitUrl());
        if (registerRequest.getGender() != null ) {
            this.validateIntRange("性别", registerRequest.getGender(),1, 2);
        } else {
            registerRequest.setGender(MerchantGenderType.MAN.code());
        }
        this.validateRequired("手机号码", registerRequest.getMobileNumber());
        if (!RegularUtil.phoneVerification(registerRequest.getMobileNumber().toString())) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
        }
        this.validateRequired("验证码" ,registerRequest.getCode());
        return buildSuccess(this.merchantCenterServiceImpl.checkMerchantBaseMessage(registerRequest));
    }


    @ApiOperation("商户发币")
    @PostMapping("/merchantIssueCurrency")
    public RootResponse<?> merchantIssueCurrency(@RequestBody MerchantAssertIssuanceMessageRequest issuanceMessageRequest) throws Exception {
        this.validateRequired("商户ID" ,issuanceMessageRequest.getMerchantId());
        this.validateStringLength("货币符号" ,issuanceMessageRequest.getCurrencySymbol(), 1 ,4);
        if (!StringUtils.isAlpha(issuanceMessageRequest.getCurrencySymbol())) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "币代号为2-3个大写字母");
        }
        if (issuanceMessageRequest.getCurrencySymbol().equalsIgnoreCase(PLATFORM_CURRENCY_SYMBOL.value())) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "该币代号不允许注册");
        }
        issuanceMessageRequest.setCurrencySymbol(issuanceMessageRequest.getCurrencySymbol().toUpperCase());
        this.validateBigDecimalRange("总发行量" ,issuanceMessageRequest.getTotal(), BigDecimal.valueOf(100), BigDecimal.valueOf(1000000000));
        this.validateBigDecimalRange("挖矿占比" ,issuanceMessageRequest.getMiningRate(), BigDecimal.valueOf(1), BigDecimal.valueOf(20));
        if (issuanceMessageRequest.getOtherMode()!=null && issuanceMessageRequest.getOtherMode()==StateType.OK_STATE.code() ) {
            issuanceMessageRequest.setFixedIncomeRate(BigDecimal.ZERO);
            issuanceMessageRequest.setStoDividendRate(BigDecimal.ZERO);
        } else {
            if (issuanceMessageRequest.getFixedIncomeRate() == null && issuanceMessageRequest.getStoDividendRate() == null)
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "请至少选择一种分红模式，若不分，红请选择”其它模式“！");
            if (issuanceMessageRequest.getFixedIncomeRate() != null)
                this.validateBigDecimalRange("固定收益占比" ,issuanceMessageRequest.getFixedIncomeRate(), BigDecimal.ZERO, BigDecimal.valueOf(1000));
            if (issuanceMessageRequest.getStoDividendRate() != null)
                this.validateBigDecimalRange("STO分红占比" ,issuanceMessageRequest.getStoDividendRate(), BigDecimal.ZERO, BigDecimal.valueOf(1000));
        }
        this.validateIntRange("收益周期(天)" ,issuanceMessageRequest.getIncomePeriod(),90,1000);
        this.validateIntRange("投资限售期(天)" ,issuanceMessageRequest.getRestrictionPeriod(),0,10000);

        this.validateStringLength("简介" ,issuanceMessageRequest.getIntroduction() , 0 , 2000);
        this.merchantCenterServiceImpl.merchantIssueCurrency(issuanceMessageRequest);
        return buildSuccess(null);
    }

    @ApiOperation("获取商户发币等相关信息")
    @GetMapping("/merchantIssueCurrencyDetails")
    @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long")
    public RootResponse<MerchantIssueCurrencyResponse> getMerchantIssueCurrencyDetails(Long merchantId) throws Exception {
        this.validateRequired("商户ID",merchantId);
        return buildSuccess(this.merchantCenterServiceImpl.getMerchantIssueCurrencyDetails(merchantId));
    }

    @ApiOperation("获取商户资产信息")
    @GetMapping("/merchantAssetDetails")
    @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long")
    public RootResponse<MerchantAssetDetailsResponse> merchantAssetDetails(Long merchantId) throws Exception{
        this.validateRequired("商户ID",merchantId);
        return buildSuccess(this.merchantCenterServiceImpl.merchantAssetDetails(merchantId));
    }

    @ApiOperation("商户当前状态")
    @GetMapping("/merchantIssueStatus")
    @ApiImplicitParam(paramType="query", name = "merchantId", value = "商户ID", required = true, dataType = "Long")
    public RootResponse<MerchantAssertIssuanceAudit> merchantIssueStatus(Long merchantId) throws Exception {
        return buildSuccess(this.merchantCenterServiceImpl.merchantIssueStatus(merchantId));
    }
}
