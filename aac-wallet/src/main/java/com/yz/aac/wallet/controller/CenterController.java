package com.yz.aac.wallet.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.request.MobileEntityRequest;
import com.yz.aac.wallet.model.response.PersonalIndexMsgResponse;
import com.yz.aac.wallet.repository.domain.UserAddressBook;
import com.yz.aac.wallet.service.PersonalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@SuppressWarnings("unused")
@Slf4j
@RestController
@Api(tags = "APP端-个人中心")
@RequestMapping(value = "/personal")
public class CenterController extends BaseController {

    @Autowired
    private PersonalService personalServiceImpl;

    @GetMapping("/")
    @ApiOperation("我的-首页信息")
    public RootResponse<PersonalIndexMsgResponse> indexMsg() throws Exception {
        return buildSuccess(this.personalServiceImpl.indexMsg(getUserId()));
    }

    @GetMapping("/AabBalance")
    @ApiOperation("实时获取我的AAB余额")
    public RootResponse<BigDecimal> getAabBalance() throws Exception {
        return buildSuccess(this.personalServiceImpl.AabBalance(getUserId()));
    }

    @PostMapping("/changePassword")
    @ApiOperation("修改支付密码")
    public RootResponse<?> changePassword(@RequestBody MobileEntityRequest mobileEntityRequest) throws Exception {
        this.validateRequired("手机" ,mobileEntityRequest.getMobile());
        this.validateRequired("验证码" ,mobileEntityRequest.getCode());
        this.validateIntRange("支付密码" ,mobileEntityRequest.getPassWord() , 100000 ,999999);

        this.personalServiceImpl.changePassword(mobileEntityRequest);
        return buildSuccess(null);
    }

    // 地址数据部分
    @GetMapping("/walletAddress")
    @ApiOperation("获取地址簿信息")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "为空时，返回地址簿中的所有信息", dataType = "String")
    public RootResponse<List<UserAddressBook>> getWalletAddress(String currencySymbol) throws Exception {
        return buildSuccess(this.personalServiceImpl.getWalletAddressForCoinType(getUserId(), currencySymbol));
    }

    @PostMapping("/addWalletAddress")
    @ApiOperation("添加地址簿")
    public RootResponse<Long> addWalletAddress(@RequestBody UserAddressBook walletAddress) throws Exception {
        walletAddress.setUserId(getUserId());
        this.validateRequired("姓名" ,walletAddress.getNikeName());
        this.validateRequired("钱包地址" ,walletAddress.getWalletAddress());
        this.validateRequired("货币符号" ,walletAddress.getCurrencySymbol());
        return buildSuccess(this.personalServiceImpl.addWalletAddress(walletAddress));
    }

    @PostMapping("/modifyWalletAddress")
    @ApiOperation("修改地址簿")
    public RootResponse<?> modifyWalletAddress(@RequestBody UserAddressBook walletAddress) throws Exception {
        walletAddress.setUserId(getUserId());
        this.validateRequired("ID" ,walletAddress.getId());
        this.validateRequired("姓名" ,walletAddress.getNikeName());
        this.validateRequired("钱包地址" ,walletAddress.getWalletAddress());
        this.validateRequired("货币符号" ,walletAddress.getCurrencySymbol());
        this.personalServiceImpl.modifyWalletAddress(walletAddress);
        return buildSuccess(null);
    }

    @DeleteMapping("/walletAddress/{id}")
    @ApiOperation("删除地址簿")
    @ApiImplicitParam(paramType="query", name = "id", value = "地址簿ID", required = true, dataType = "Integer")
    public RootResponse<?> delWalletAddress(@PathVariable Integer id) throws Exception {
        // 数据校验
        this.validateRequired("地址簿ID" ,id);
        this.personalServiceImpl.delWalletAddress(id, getUserId());
            return buildSuccess(null);
    }

    @GetMapping("/getAllCurrencySymbol")
    @ApiOperation("获取平台所有币种-List<String>")
    public RootResponse<List<String>> getAllCurrencySymbol() throws Exception {
        return buildSuccess(this.personalServiceImpl.getAllCurrencySymbol());
    }

    @GetMapping("/getNameByWalletAddress")
    @ApiOperation("根据地址获取姓名")
    @ApiImplicitParam(paramType="query", name = "walletAddress", value = "根据地址获取姓名", dataType = "String")
    public RootResponse<String> getNameByWalletAddress(String walletAddress) throws Exception {
        return buildSuccess(this.personalServiceImpl.getNameByWalletAddress(walletAddress));
    }

    private Long getUserId() {
        return this.getLoginInfo().getId();
    }
}