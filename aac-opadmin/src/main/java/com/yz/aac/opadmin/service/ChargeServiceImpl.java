package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryChargeResponse;
import com.yz.aac.opadmin.repository.MerchantAssertIssuanceRepository;
import com.yz.aac.opadmin.repository.PlatformServiceChargeStrategyRepository;
import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuance;
import com.yz.aac.opadmin.repository.domain.PlatformServiceChargeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;

@Service
@Slf4j
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    private PlatformServiceChargeStrategyRepository strategyRepository;

    @Autowired
    private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;

    @Override
    public QueryChargeResponse queryCharges(QueryChargeRequest request, Long loginId) {
        PlatformServiceChargeStrategy param = new PlatformServiceChargeStrategy();
        param.setName(request.getName());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<PlatformServiceChargeStrategy> page = (Page<PlatformServiceChargeStrategy>) strategyRepository.query(param);
        List<QueryChargeResponse.Item> items = page.getResult()
                .stream()
                .map(x -> new QueryChargeResponse.Item(
                        x.getId(),
                        x.getName(),
                        x.getTradeChargeRate(),
                        x.getIssuanceDeposit(),
                        x.getIsDefault()
                )).collect(Collectors.toList());
        return new QueryChargeResponse(page.getTotal(), items);
    }

    @Override
    public void createCharge(CreateChargeRequest request) throws Exception {
        //检查名称是否重复
        PlatformServiceChargeStrategy param = new PlatformServiceChargeStrategy();
        param.setAccurateName(request.getName().trim());
        if (!strategyRepository.query(param).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //检查受否已存在默认项
        param = new PlatformServiceChargeStrategy();
        param.setIsDefault(YES.value());
        int isDefault = strategyRepository.query(param).isEmpty() ? YES.value() : NO.value();
        //写入
        long sysDate = System.currentTimeMillis();
        strategyRepository.store(new PlatformServiceChargeStrategy(
                null,
                request.getName().trim(),
                null,
                request.getTradeChargeRate(),
                new BigDecimal(request.getIssuanceDeposit()),
                isDefault,
                sysDate,
                sysDate
        ));
    }

    @Override
    public void updateCharge(UpdateChargeRequest request) throws Exception {
        //检查名称是否重复
        PlatformServiceChargeStrategy param = new PlatformServiceChargeStrategy();
        param.setAccurateName(request.getName().trim());
        List<PlatformServiceChargeStrategy> items = strategyRepository.query(param);
        if (!items.isEmpty() && items.iterator().next().getId() != request.getId().longValue()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //查询
        param = new PlatformServiceChargeStrategy();
        param.setId(request.getId());
        items = strategyRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        PlatformServiceChargeStrategy item = items.iterator().next();
        //更新
        long sysDate = System.currentTimeMillis();
        strategyRepository.update(new PlatformServiceChargeStrategy(
                item.getId(),
                request.getName().trim(),
                null,
                request.getTradeChargeRate(),
                new BigDecimal(request.getIssuanceDeposit()),
                item.getIsDefault(),
                item.getCreateTime(),
                sysDate
        ));
    }

    @Override
    public void deleteCharge(DeleteChargeRequest request) throws Exception {
        PlatformServiceChargeStrategy param = new PlatformServiceChargeStrategy();
        param.setId(request.getId());
        List<PlatformServiceChargeStrategy> items = strategyRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        PlatformServiceChargeStrategy item = items.iterator().next();
        if (item.getIsDefault() == YES.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DELETE_DEFAULT_DENY.value());
        }
        //不能删除正在使用中的配置项
        PageHelper.startPage(1, 1);
        MerchantAssertIssuance condition = new MerchantAssertIssuance();
        condition.setServiceChargeId(request.getId());
        if (!merchantAssertIssuanceRepository.query(condition).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DELETE_USING_DENY.value());
        }
        strategyRepository.delete(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyDefaultCharge(ApplyDefaultChargeRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        //获取新默认项
        PlatformServiceChargeStrategy param = new PlatformServiceChargeStrategy();
        param.setId(request.getId());
        List<PlatformServiceChargeStrategy> newItems = strategyRepository.query(param);
        if (newItems.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        PlatformServiceChargeStrategy newItem = newItems.iterator().next();
        //更新旧默认项
        param = new PlatformServiceChargeStrategy();
        param.setIsDefault(YES.value());
        List<PlatformServiceChargeStrategy> oldItems = strategyRepository.query(param);
        if (!oldItems.isEmpty()) {
            PlatformServiceChargeStrategy oldItem = oldItems.iterator().next();
            //若新旧默认项是同一条数据，则无需更新，直接结束
            if (newItem.getId().longValue() == oldItem.getId()) {
                return;
            }
            oldItem.setIsDefault(NO.value());
            oldItem.setUpdateTime(sysDate);
            strategyRepository.update(oldItem);
        }
        //更新新默认项
        newItem.setIsDefault(YES.value());
        newItem.setUpdateTime(sysDate);
        strategyRepository.update(newItem);
    }
}
