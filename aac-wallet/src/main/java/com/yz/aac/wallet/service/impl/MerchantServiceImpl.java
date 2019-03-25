package com.yz.aac.wallet.service.impl;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.wallet.Constants.IssuanceAuditStatus;
import com.yz.aac.wallet.Constants.PlatformAssertTradeExamineType;
import com.yz.aac.wallet.Constants.PlatformAssertTradeType;
import com.yz.aac.wallet.model.request.AABOrderInfoRequest;
import com.yz.aac.wallet.model.response.AABOrderInfoResponse;
import com.yz.aac.wallet.model.response.MerchantDepositStatusResponse;
import com.yz.aac.wallet.repository.MerchantAssertIssuanceAuditRepository;
import com.yz.aac.wallet.repository.MerchantAssertIssuanceRepository;
import com.yz.aac.wallet.repository.MerchantRepository;
import com.yz.aac.wallet.repository.OrderReferenceNumberRepository;
import com.yz.aac.wallet.repository.PlatformAssertSellerRepository;
import com.yz.aac.wallet.repository.PlatformAssertSellingOrderRepository;
import com.yz.aac.wallet.repository.PlatformAssertTradeRecordRepository;
import com.yz.aac.wallet.repository.PlatformServiceChargeStrategyRepository;
import com.yz.aac.wallet.repository.UserAssetsRepository;
import com.yz.aac.wallet.repository.UserRepository;
import com.yz.aac.wallet.repository.domain.Merchant;
import com.yz.aac.wallet.repository.domain.MerchantAssertIssuance;
import com.yz.aac.wallet.repository.domain.OrderReferenceNumber;
import com.yz.aac.wallet.repository.domain.PlatformAssertSeller;
import com.yz.aac.wallet.repository.domain.PlatformAssertSellingOrder;
import com.yz.aac.wallet.repository.domain.PlatformAssertTradeRecord;
import com.yz.aac.wallet.repository.domain.PlatformServiceChargeStrategy;
import com.yz.aac.wallet.repository.domain.User;
import com.yz.aac.wallet.repository.domain.UserAssets;
import com.yz.aac.wallet.service.MerchantService;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;
	
	@Autowired
	private PlatformAssertSellerRepository platformAssertSellerRepository;
	
	@Autowired
	private PlatformServiceChargeStrategyRepository platformServiceChargeStrategyRepository;
	
	@Autowired
	private OrderReferenceNumberRepository orderReferenceNumberRepository;
	
	@Autowired
	private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;
	
	@Autowired
	private UserAssetsRepository userAssertRepository;
	
	@Autowired
	private MerchantAssertIssuanceAuditRepository merchantAssertIssuanceAuditRepository;
	
	@Autowired
    private FileStorageHandler fileStorageHandler;
	
	@Autowired
	private PlatformAssertSellingOrderRepository platformAssertSellingOrderRepository;
	
	@Override
	public AABOrderInfoResponse getDepositMes(Long userId) throws Exception {
		try{
			Integer status = merchantAssertIssuanceAuditRepository.getStatusByUserId(userId);
			if (IssuanceAuditStatus.DEPOSIT_VERIFY.code() == status) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "已经缴纳押金请等待审核！");
			} else if (IssuanceAuditStatus.DEPOSIT_YES.code() == status) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "已缴纳过押金！");
			}
			
			//1.随机获取足够平台币挂单信息
			User user = userRepository.getUserById(userId);
			
			Merchant merchant = merchantRepository.getMerchantByMobile(user.getMobileNumber());
			MerchantAssertIssuance mair = merchantAssertIssuanceRepository.getAllByMerchantId(merchant.getId());
			PlatformServiceChargeStrategy pscs = platformServiceChargeStrategyRepository.getById(mair.getServiceChargeId());
			
			PlatformAssertSellingOrder paso = platformAssertSellingOrderRepository.getAssertSellerOrderRandom(pscs.getIssuanceDeposit());
			if (null == paso) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "平台币货源不足，无法进行充值！");
			}
			
			//2.根据挂单ID获取挂单人信息
			PlatformAssertSeller pas = platformAssertSellerRepository.getPlatformAssertSellerById(paso.getSellerId());
			AABOrderInfoResponse aabOrder = new AABOrderInfoResponse(
					pas.getId(), pas.getName(), pas.getSupportAlipay(), pas.getSupportWechat(),
					pas.getSupportBankCard(), pas.getAlipayAccount(), pas.getAlipayQrCodePath(),
					pas.getWechatAccount(), pas.getWechatQrCodePath(), 
					pas.getBankCardNumber(), paso.getId());
			if (!StringUtils.isEmpty(aabOrder.getAlipayQrCodePath())) {
				aabOrder.setAlipayQrCodePath(fileStorageHandler.genDownloadUrl(aabOrder.getAlipayQrCodePath()));
			}
			
			if (!StringUtils.isEmpty(aabOrder.getWechatQrCodePath())) {
				aabOrder.setWechatQrCodePath(fileStorageHandler.genDownloadUrl(aabOrder.getWechatQrCodePath()));
			}
			
			//3.押金交易金额
			aabOrder.setTransactionNum(pscs.getIssuanceDeposit());
			
			//4.AAB兑换率
			aabOrder.setUnitPrice(paso.getRmbPrice());
			
			//5.付款参考号
			boolean condition = true;
			while(condition){
				try{
					aabOrder.setReferenceNumber(RandomUtil.createNumber(6));
					orderReferenceNumberRepository.saveReferenceNumber(new OrderReferenceNumber(aabOrder.getReferenceNumber()));
					condition = false;
				} catch(DuplicateKeyException du){
					condition = true;
					log.info("付款参考码重复，重新生成！");
				}
			}
			//6.订单时间
			aabOrder.setOrderTime(System.currentTimeMillis());
			
			aabOrder.setInitiatorId(user.getId());
			aabOrder.setInitiatorName(user.getName());
			aabOrder.setTradeType(PlatformAssertTradeType.DEPOSIT.code());
			
			UserAssets uAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
			aabOrder.setWalletAddress(uAssert.getWalletAddress());
			
			return aabOrder;
		} catch (NullPointerException e) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您还不是商户无法交易！");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AABOrderInfoResponse addDepositOrder(AABOrderInfoRequest orderInfo) throws Exception {
		synchronized (this) {
			Integer status = merchantAssertIssuanceAuditRepository.getStatusByUserId(orderInfo.getInitiatorId());
			if (IssuanceAuditStatus.DEPOSIT_VERIFY.code() == status) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "已经缴纳押金请等待审核！");
			} else if (IssuanceAuditStatus.DEPOSIT_YES.code() == status) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "已缴纳过押金！");
			}
		}
		
		PlatformAssertSellingOrder paso = platformAssertSellingOrderRepository.getAssertSellerOrder(orderInfo.getSellingOrderId());
		
		PlatformAssertTradeRecord patr = 
				new PlatformAssertTradeRecord(
						orderInfo.getInitiatorId(), orderInfo.getInitiatorName(), 
						orderInfo.getTradeType(), orderInfo.getOrderTime(), orderInfo.getUnitPrice(),
						orderInfo.getTransactionNum(), orderInfo.getWalletAddress(), BigDecimal.ZERO, BigDecimal.ZERO,
						orderInfo.getPartnerId(), orderInfo.getPartnerName(), 
						orderInfo.getReferenceNumber(), PlatformAssertTradeExamineType.TO_BE_TRANSFERRED.code(),
						orderInfo.getSellingOrderId(), paso.getAvailableTradeAmount(), 
						paso.getMinAmountLimit(), paso.getMaxAmountLimit());
		PlatformAssertTradeRecord patRecord = platformAssertTradeRecordRepository.getAssertTradeRecord(orderInfo.getTradeType(), orderInfo.getInitiatorId());
		if (null == patRecord) {
			platformAssertTradeRecordRepository.saveAssertTeadeTrcord(patr);
		} else {
			platformAssertTradeRecordRepository.updatePlatformAssertTrade(patr);
			
		}
		
		//同时更新对应商户账号发币审核状态
		merchantAssertIssuanceAuditRepository.updateStatusByUserId(orderInfo.getInitiatorId(), IssuanceAuditStatus.DEPOSIT_VERIFY.code());
		
		AABOrderInfoResponse aabOrder = new AABOrderInfoResponse();
		BeanUtils.copyProperties(orderInfo, aabOrder);
		
		return aabOrder;
	}

	@Override
	public MerchantDepositStatusResponse getDepositStatus(Long userId) throws Exception {
		
		return new MerchantDepositStatusResponse(merchantAssertIssuanceAuditRepository.getStatusByUserId(userId));
	}
}
