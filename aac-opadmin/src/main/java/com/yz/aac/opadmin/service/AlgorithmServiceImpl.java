package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryAlgorithmResponse;
import com.yz.aac.opadmin.repository.AlgorithmRepository;
import com.yz.aac.opadmin.repository.UserPropertyRepository;
import com.yz.aac.opadmin.repository.domain.IncreaseStrategy;
import com.yz.aac.opadmin.repository.domain.UserProperty;
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
public class AlgorithmServiceImpl implements AlgorithmService {

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    @Override
    public QueryAlgorithmResponse queryAlgorithms(QueryAlgorithmRequest request) {
        IncreaseStrategy param = new IncreaseStrategy();
        param.setName(request.getName());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<IncreaseStrategy> page = (Page<IncreaseStrategy>) algorithmRepository.query(param);
        List<QueryAlgorithmResponse.Item> items = page.getResult()
                .stream().map(x -> new QueryAlgorithmResponse.Item(
                        x.getId(),
                        x.getName(),
                        x.getIncreasedPowerPoint(),
                        x.getConsumedAd(),
                        x.getPlatformCurrency(),
                        x.getIsDefault()
                )).collect(Collectors.toList());
        return new QueryAlgorithmResponse(page.getTotal(), items);
    }

    @Override
    public void createAlgorithm(CreateAlgorithmRequest request) throws Exception {
        //检查名称是否重复
        IncreaseStrategy param = new IncreaseStrategy();
        param.setAccurateName(request.getName().trim());
        if (!algorithmRepository.query(param).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //检查受否已存在默认项
        param = new IncreaseStrategy();
        param.setIsDefault(YES.value());
        int isDefault = algorithmRepository.query(param).isEmpty() ? YES.value() : NO.value();
        //写入
        long sysDate = System.currentTimeMillis();
        algorithmRepository.store(new IncreaseStrategy(
                null,
                request.getName().trim(),
                null,
                request.getIncreasedPowerPoint(),
                request.getConsumedAd(),
                new BigDecimal(request.getPlatformCurrency()),
                isDefault,
                sysDate,
                sysDate
        ));
    }

    @Override
    public void updateAlgorithm(UpdateAlgorithmRequest request) throws Exception {
        //检查名称是否重复
        IncreaseStrategy param = new IncreaseStrategy();
        param.setAccurateName(request.getName().trim());
        List<IncreaseStrategy> items = algorithmRepository.query(param);
        if (!items.isEmpty() && items.iterator().next().getId() != request.getId().longValue()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //查询
        param = new IncreaseStrategy();
        param.setId(request.getId());
        items = algorithmRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        IncreaseStrategy item = items.iterator().next();
        //更新
        long sysDate = System.currentTimeMillis();
        algorithmRepository.update(new IncreaseStrategy(
                item.getId(),
                request.getName().trim(),
                null,
                request.getIncreasedPowerPoint(),
                request.getConsumedAd(),
                new BigDecimal(request.getPlatformCurrency()),
                item.getIsDefault(),
                item.getCreateTime(),
                sysDate
        ));
    }

    @Override
    public void deleteAlgorithm(DeleteAlgorithmRequest request) throws Exception {
        IncreaseStrategy param = new IncreaseStrategy();
        param.setId(request.getId());
        List<IncreaseStrategy> items = algorithmRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        IncreaseStrategy item = items.iterator().next();
        if (item.getIsDefault() == YES.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DELETE_DEFAULT_DENY.value());
        }
        //不能删除正在使用中的算法
        PageHelper.startPage(1, 1);
        UserProperty condition = new UserProperty(null, null, null, request.getId(), null, null);
        if (!userPropertyRepository.query(condition).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DELETE_USING_DENY.value());
        }
        algorithmRepository.delete(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyDefaultAlgorithm(ApplyDefaultAlgorithmRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        //获取新默认项
        IncreaseStrategy param = new IncreaseStrategy();
        param.setId(request.getId());
        List<IncreaseStrategy> newItems = algorithmRepository.query(param);
        if (newItems.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        IncreaseStrategy newItem = newItems.iterator().next();
        //更新旧默认项
        param = new IncreaseStrategy();
        param.setIsDefault(YES.value());
        List<IncreaseStrategy> oldItems = algorithmRepository.query(param);
        if (!oldItems.isEmpty()) {
            IncreaseStrategy oldItem = oldItems.iterator().next();
            //若新旧默认项是同一条数据，则无需更新，直接结束
            if (newItem.getId().longValue() == oldItem.getId()) {
                return;
            }
            oldItem.setIsDefault(NO.value());
            oldItem.setUpdateTime(sysDate);
            algorithmRepository.update(oldItem);
        }
        //更新新默认项
        newItem.setIsDefault(YES.value());
        newItem.setUpdateTime(sysDate);
        algorithmRepository.update(newItem);
    }
}
