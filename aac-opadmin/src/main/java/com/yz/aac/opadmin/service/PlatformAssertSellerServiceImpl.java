package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.exception.SerializationException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.opadmin.model.request.CreatePlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.request.DeletePlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.request.QueryPlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.request.UpdatePlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.response.QueryPlatformAssertSellerResponse;
import com.yz.aac.opadmin.repository.OperatorActionLogRepository;
import com.yz.aac.opadmin.repository.PlatformAssertSellerRepository;
import com.yz.aac.opadmin.repository.PlatformAssertSellingOrderRepository;
import com.yz.aac.opadmin.repository.domain.OperatorActionLog;
import com.yz.aac.opadmin.repository.domain.PlatformAssertSeller;
import com.yz.aac.opadmin.repository.domain.PlatformAssertSellingOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;
import static com.yz.aac.opadmin.Constants.FeatureModuleAction.*;

@Service
@Slf4j
public class PlatformAssertSellerServiceImpl implements PlatformAssertSellerService {

    @Autowired
    private PlatformAssertSellerRepository platformAssertSellerRepository;

    @Autowired
    private PlatformAssertSellingOrderRepository platformAssertSellingOrderRepository;

    @Autowired
    private FileStorageHandler fileStorageHandler;

    @Autowired
    private OperatorActionLogRepository logRepository;

    @Override
    public QueryPlatformAssertSellerResponse queryPlatformAssertSellers(QueryPlatformAssertSellerRequest request) {
        PlatformAssertSeller param = new PlatformAssertSeller();
        param.setName(request.getName());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<PlatformAssertSeller> page = (Page<PlatformAssertSeller>) platformAssertSellerRepository.query(param);
        List<QueryPlatformAssertSellerResponse.Item> items = page.getResult()
                .stream()
                .map(x -> {
                    try {
                        return new QueryPlatformAssertSellerResponse.Item(
                                x.getId(),
                                x.getName(),
                                x.getSupportAlipay(),
                                x.getSupportWechat(),
                                x.getSupportBankCard(),
                                x.getAlipayAccount(),
                                x.getSupportAlipay() == YES.value() ? fileStorageHandler.genDownloadUrl(x.getAlipayQrCodePath()) : null,
                                x.getWechatAccount(),
                                x.getSupportWechat() == YES.value() ? fileStorageHandler.genDownloadUrl(x.getWechatQrCodePath()) : null,
                                x.getBankCardNumber(),
                                x.getTotalSoldCurrency(),
                                x.getTotalSoldCount()
                        );
                    } catch (SerializationException e) {
                        log.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        return new QueryPlatformAssertSellerResponse(page.getTotal(), items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPlatformAssertSeller(CreatePlatformAssertSellerRequest request) throws Exception {
        //检查名称是否重复
        PlatformAssertSeller param = new PlatformAssertSeller();
        param.setAccurateName(request.getName().trim());
        if (!platformAssertSellerRepository.query(param).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //写入
        long sysDate = System.currentTimeMillis();
        PlatformAssertSeller seller = new PlatformAssertSeller(
                null,
                request.getName().trim(),
                null,
                request.getSupportAlipay() ? YES.value() : NO.value(),
                request.getSupportWechat() ? YES.value() : NO.value(),
                request.getSupportBankCard() ? YES.value() : NO.value(),
                request.getSupportAlipay() ? request.getAlipayAccount().trim() : null,
                request.getSupportAlipay() ? fileStorageHandler.uploadFile(request.getAlipayQrCodeIcon().getContent(), request.getAlipayQrCodeIcon().getExtName()) : null,
                request.getSupportWechat() ? request.getWechatAccount().trim() : null,
                request.getSupportWechat() ? fileStorageHandler.uploadFile(request.getWechatQrCodeIcon().getContent(), request.getWechatQrCodeIcon().getExtName()) : null,
                request.getSupportBankCard() ? request.getBankCardNumber() : null,
                new BigDecimal(0),
                0,
                sysDate,
                sysDate
        );
        platformAssertSellerRepository.store(seller);
        //写入操作员访问日志
        logRepository.store(new OperatorActionLog(
                null,
                request.getLoginId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_ADD_SELLER.moduleId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_ADD_SELLER.actionId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_ADD_SELLER.additionalInfo()
                        .replaceFirst(PLACEHOLDER.value(), seller.getName())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isBlank(seller.getAlipayAccount()) ? NONE.value() : seller.getAlipayAccount())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isBlank(seller.getWechatAccount()) ? NONE.value() : seller.getWechatAccount())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isBlank(seller.getBankCardNumber()) ? NONE.value() : seller.getBankCardNumber()),
                sysDate,
                null,
                null
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlatformAssertSeller(UpdatePlatformAssertSellerRequest request) throws Exception {
        //检查名称是否重复
        PlatformAssertSeller param = new PlatformAssertSeller();
        param.setAccurateName(request.getName().trim());
        List<PlatformAssertSeller> items = platformAssertSellerRepository.query(param);
        if (!items.isEmpty() && items.iterator().next().getId() != request.getId().longValue()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //查询
        param = new PlatformAssertSeller();
        param.setId(request.getId());
        items = platformAssertSellerRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        PlatformAssertSeller item = items.iterator().next();
        //更新
        long sysDate = System.currentTimeMillis();
        String alipayQrCodeIconPath = item.getAlipayQrCodePath();
        if (request.getSupportAlipay() && request.getUpdateAlipayQrCodeIcon()) {
            fileStorageHandler.deleteFile(alipayQrCodeIconPath);
            alipayQrCodeIconPath = fileStorageHandler.uploadFile(request.getAlipayQrCodeIcon().getContent(), request.getAlipayQrCodeIcon().getExtName());
        }
        String wechatQrCodeIconPath = item.getWechatQrCodePath();
        if (request.getSupportWechat() && request.getUpdateWechatQrCodeIcon()) {
            fileStorageHandler.deleteFile(wechatQrCodeIconPath);
            wechatQrCodeIconPath = fileStorageHandler.uploadFile(request.getWechatQrCodeIcon().getContent(), request.getWechatQrCodeIcon().getExtName());
        }
        PlatformAssertSeller seller = new PlatformAssertSeller(
                request.getId(),
                request.getName().trim(),
                null,
                request.getSupportAlipay() ? YES.value() : NO.value(),
                request.getSupportWechat() ? YES.value() : NO.value(),
                request.getSupportBankCard() ? YES.value() : NO.value(),
                request.getSupportAlipay() ? request.getAlipayAccount().trim() : item.getAlipayAccount(),
                alipayQrCodeIconPath,
                request.getSupportWechat() ? request.getWechatAccount().trim() : item.getWechatAccount(),
                wechatQrCodeIconPath,
                request.getSupportBankCard() ? request.getBankCardNumber() : item.getBankCardNumber(),
                item.getTotalSoldCurrency(),
                item.getTotalSoldCount(),
                item.getCreateTime(),
                sysDate
        );
        platformAssertSellerRepository.update(seller);
        //写入操作员访问日志
        logRepository.store(new OperatorActionLog(
                null,
                request.getLoginId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_MODIFY_SELLER.moduleId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_MODIFY_SELLER.actionId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_MODIFY_SELLER.additionalInfo()
                        .replaceFirst(PLACEHOLDER.value(), seller.getName())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isBlank(seller.getAlipayAccount()) ? NONE.value() : seller.getAlipayAccount())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isBlank(seller.getWechatAccount()) ? NONE.value() : seller.getWechatAccount())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isBlank(seller.getBankCardNumber()) ? NONE.value() : seller.getBankCardNumber()),
                sysDate,
                null,
                null
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlatformAssertSeller(DeletePlatformAssertSellerRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        PlatformAssertSeller param = new PlatformAssertSeller();
        param.setId(request.getId());
        List<PlatformAssertSeller> items = platformAssertSellerRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        //若该人员名下已有挂出的卖单，则不能删除
        PlatformAssertSellingOrder order = new PlatformAssertSellingOrder();
        order.setSellerId(request.getId());
        if (!platformAssertSellingOrderRepository.query(order).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DELETE_SELLER_DENY.value());
        }
        PlatformAssertSeller seller = items.iterator().next();
        fileStorageHandler.deleteFile(seller.getAlipayQrCodePath());
        fileStorageHandler.deleteFile(seller.getWechatQrCodePath());
        platformAssertSellerRepository.delete(request.getId());
        //写入操作员访问日志
        logRepository.store(new OperatorActionLog(
                null,
                request.getLoginId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_REMOVE_SELLER.moduleId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_REMOVE_SELLER.actionId(),
                PLATFORM_CURRENCY_SELLER_CONFIG_REMOVE_SELLER.additionalInfo()
                        .replaceFirst(PLACEHOLDER.value(), seller.getName()),
                sysDate,
                null,
                null
        ));
    }

}
