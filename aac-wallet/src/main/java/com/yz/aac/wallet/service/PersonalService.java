package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.request.ExchangeRequest;
import com.yz.aac.wallet.model.request.MobileEntityRequest;
import com.yz.aac.wallet.model.request.TransferToWalletAddrRequest;
import com.yz.aac.wallet.model.response.*;
import com.yz.aac.wallet.repository.domain.UserAddressBook;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface PersonalService {

    /**
     * 获取用户基础信息
     * @param userId 用户ID
     * @return
     */
    PersonalAssetsMsgResponse getPersonalBaseMsg(Long userId , String coinType) throws Exception;

    /**
     * 获取用户交易记录
     * @param userId 用户ID
     * @return
     */
    List<TradeRecordResponse> getTransactionRecord(Long userId, String currencySymbol) throws Exception;

    /**
     * 转账
     * @return
     */
    void transferToWalletAddr(TransferToWalletAddrRequest transferToWalletAddr) throws Exception;

    /**
     * 根据地址获取姓名
     * @param walletAddress
     * @return
     */
    String getNameByWalletAddress(String walletAddress) throws Exception;

    /**
     * 获取我的AAB余额
     * @param userId 用户ID
     * @return
     */
    BigDecimal AabBalance(Long userId) throws Exception;

    /**
     * 获取我的其他币余量
     * @param userId 用户ID
     * @return
     */
    OtherCoinMsgResponse getOtherAssets(Long userId) throws Exception;


    /**
     * 获取商家二维码
     * @param currencySymbol 商户币类型
     * @return
     */
    String getMchQrCode(String currencySymbol) throws Exception;

    /**
     * 获取冻结币资产相关信息
     * @param userId 用户ID
     * @return
     */
    List<CoinDetailsMsgResponse> getFreezeAssets(Long userId) throws Exception;

    /**
     * 返回指定币类型的地址簿信息(为空时返回全部)
     * @param userId 用户ID
     * @param currencySymbol 币类型
     * @return
     */
    List<UserAddressBook> getWalletAddressForCoinType(Long userId, String currencySymbol) throws Exception;

    /**
     * 添加钱包地址信息
     * @param walletAddress 钱包地址信息
     * @throws Exception
     */
    Long addWalletAddress(UserAddressBook walletAddress) throws Exception;

    /**
     * 修改钱包地址信息
     * @param walletAddress
     * @return
     */
    void modifyWalletAddress(UserAddressBook walletAddress) throws Exception ;

    /**
     * 删除钱包地址信息
     * @param id 钱包地址ID
     * @throws Exception
     */
    void delWalletAddress(Integer id, Long userId) throws Exception;

    /**
     * 修改用户密码
     * @param mobileEntityRequest
     */
    void changePassword(MobileEntityRequest mobileEntityRequest) throws Exception;

    /**
     * 获取所有的货币信息
     * @return
     * @throws Exception
     */
    List<String> getAllCurrencySymbol() throws Exception;

    /**
     * 获取我的-首页信息
     * @param userId
     * @return
     * @throws Exception
     */
    PersonalIndexMsgResponse indexMsg(Long userId) throws Exception;

    /**
     * 兑换
     * @param exchangeRequest
     * @param userId
     * @return
     * @throws Exception
     */
    String exchange(ExchangeRequest exchangeRequest, Long userId) throws Exception;

    /**
     * 兑换服务列表
     * @param userId
     * @return
     * @throws Exception
     */
    Set<ActivityListResponse> activityList(Long userId) throws Exception;

    /**
     * 兑换服务详情
     * @param category
     * @param userId
     * @return
     * @throws Exception
     */
    ExchangeServiceInfoResponse serviceInfo(Integer category, Long userId) throws Exception;

}
