package com.yz.aac.wallet.service.impl;

import com.yz.aac.wallet.repository.MerchantAssertStatisticsRepository;
import com.yz.aac.wallet.service.MerchantAssertStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MerchantAssertStatisticsServiceImpl implements MerchantAssertStatisticsService {

    @Autowired
    private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBehaviourStatistics(String coinType, String key, int addValue) throws Exception {
        this.merchantAssertStatisticsRepository.addBehaviourStatistics(coinType,key,addValue);
    }

}
