package com.yz.aac.opadmin.task;

import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.repository.PlatformAssertIncomeExpenditureRecordRepository;
import com.yz.aac.opadmin.repository.PlatformAssetStatisticsRepository;
import com.yz.aac.opadmin.repository.UserMiningRecordRepository;
import com.yz.aac.opadmin.repository.domain.CurrencyKeyValuePair;
import com.yz.aac.opadmin.repository.domain.PlatformAssertIncomeExpenditureRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.opadmin.Constants.LedgerInOut.OUT;
import static com.yz.aac.opadmin.Constants.LedgerType.TRANSFER_PLATFORM_CURRENCY;
import static com.yz.aac.opadmin.Constants.PlatformAssertStatistics.*;

/**
 * 平台资产统计相关定时任务
 */

@Slf4j
@Component
public class DashboardTask {

    @Autowired
    private PlatformAssertIncomeExpenditureRecordRepository incomeExpenditureRepository;

    @Autowired
    private PlatformAssetStatisticsRepository statisticsRepository;

    @Autowired
    private UserMiningRecordRepository userMiningRecordRepository;

    /**
     * 更新平台资产统计信息
     * 每日0,8,16点执行
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Scheduled(cron = "0 0 0,8,16 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updatePlatformAssetStatistcs() {
        try {
            log.info("====更新平台资产统计信息====>>");
            List<CurrencyKeyValuePair> pairs = statisticsRepository.query();
            //时间
            CurrencyKeyValuePair updateTime = pairs.stream().filter(x -> UPDATE_TIME.value().equals(x.getKey())).findFirst().get();
            //活跃自然增长币总量
            CurrencyKeyValuePair activeIncrease = pairs.stream().filter(x -> ACTIVE_INCREASE.value().equals(x.getKey())).findFirst().get();
            //活跃自然增长币已挖量
            CurrencyKeyValuePair activeIncreaseMined = pairs.stream().filter(x -> ACTIVE_INCREASE_MINED.value().equals(x.getKey())).findFirst().get();
            //活跃自然增长币剩余量
            CurrencyKeyValuePair activeIncreaseRest = pairs.stream().filter(x -> ACTIVE_INCREASE_REST.value().equals(x.getKey())).findFirst().get();
            //活跃可挖币总量
            CurrencyKeyValuePair activeMining = pairs.stream().filter(x -> ACTIVE_MINING.value().equals(x.getKey())).findFirst().get();
            //活跃可挖币已挖量
            CurrencyKeyValuePair activeMiningMined = pairs.stream().filter(x -> ACTIVE_MINING_MINED.value().equals(x.getKey())).findFirst().get();
            //活跃可挖币剩余量
            CurrencyKeyValuePair activeMiningRest = pairs.stream().filter(x -> ACTIVE_MINING_REST.value().equals(x.getKey())).findFirst().get();
            //固定币总量
            CurrencyKeyValuePair fixed = pairs.stream().filter(x -> FIXED.value().equals(x.getKey())).findFirst().get();
            //固定币已售出量
            CurrencyKeyValuePair fixedSold = pairs.stream().filter(x -> FIXED_SOLD.value().equals(x.getKey())).findFirst().get();
            //固定币剩余量
            CurrencyKeyValuePair fixedRest = pairs.stream().filter(x -> FIXED_REST.value().equals(x.getKey())).findFirst().get();
            //更新时间
            updateTime.setValue(BigDecimal.valueOf(System.currentTimeMillis()));
            statisticsRepository.update(updateTime);
            //更新活跃自然增长币
            BigDecimal activeIncreaseMinedValue = userMiningRecordRepository.sumBonus(
                    Constants.MiningBonusType.PLATFORM_CURRENCY.value(),
                    Collections.singletonList(Constants.MiningActionType.INCREASE.value())
            );
            activeIncreaseMined.setValue(activeIncreaseMinedValue);
            activeIncreaseRest.setValue(activeIncrease.getValue().subtract(activeIncreaseMined.getValue()));
            statisticsRepository.update(activeIncreaseMined);
            statisticsRepository.update(activeIncreaseRest);
            //更新活跃挖币
            BigDecimal activeMiningMinedValue = userMiningRecordRepository.sumBonus(
                    Constants.MiningBonusType.PLATFORM_CURRENCY.value(),
                    Arrays.stream(Constants.MiningActionType.values())
                            .filter(x -> x.value() != Constants.MiningActionType.RED_PACKET.value() && x.value() != Constants.MiningActionType.INCREASE.value())
                            .map(Constants.MiningActionType::value)
                            .collect(Collectors.toList())
            );
            activeMiningMined.setValue(activeMiningMinedValue);
            activeMiningRest.setValue(activeMining.getValue().subtract(activeMiningMined.getValue()));
            statisticsRepository.update(activeMiningMined);
            statisticsRepository.update(activeMiningRest);
            //更新固定币
            PlatformAssertIncomeExpenditureRecord param = new PlatformAssertIncomeExpenditureRecord();
            param.setDirection(OUT.value());
            param.setAction(TRANSFER_PLATFORM_CURRENCY.value());
            List<PlatformAssertIncomeExpenditureRecord> items = incomeExpenditureRepository.querySumAmount(param);
            if (!items.isEmpty()) {
                PlatformAssertIncomeExpenditureRecord item = items.iterator().next();
                if (null != item) {
                    fixedSold.setValue(item.getSumAmount());
                    fixedRest.setValue(fixed.getValue().subtract(item.getSumAmount()));
                    statisticsRepository.update(fixedSold);
                    statisticsRepository.update(fixedRest);
                }
            }
            log.info("====更新平台资产统计信息====<<");
        } catch (Exception e) {
            log.error("====更新平台资产统计信息====", e);
        }
    }

}
