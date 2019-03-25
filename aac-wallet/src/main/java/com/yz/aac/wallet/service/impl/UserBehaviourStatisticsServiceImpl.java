package com.yz.aac.wallet.service.impl;

import com.yz.aac.wallet.repository.UserBehaviourStatisticsRepository;
import com.yz.aac.wallet.repository.domain.UserBehaviourStatistics;
import com.yz.aac.wallet.service.UserBehaviourStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserBehaviourStatisticsServiceImpl implements UserBehaviourStatisticsService {

    @Autowired
    private UserBehaviourStatisticsRepository userBehaviourStatisticsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBehaviourStatistics(Long userId, String key, int addValue) throws Exception {
        if (this.userBehaviourStatisticsRepository.addBehaviourStatistics(userId,key,addValue)<1) {
            // 初始化数据
            add(new UserBehaviourStatistics(null,userId,key,addValue));
        }
    }

    private void add (UserBehaviourStatistics userBehaviourStatistics){
        this.userBehaviourStatisticsRepository.add(userBehaviourStatistics);
    }
}
