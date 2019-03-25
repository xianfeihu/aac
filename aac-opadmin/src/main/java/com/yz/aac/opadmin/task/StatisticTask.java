package com.yz.aac.opadmin.task;

import com.yz.aac.common.util.DateUtil;
import com.yz.aac.opadmin.repository.MerchantAssertStatisticsRepository;
import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuance;
import com.yz.aac.opadmin.repository.domain.MerchantAssertStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static java.util.Calendar.DATE;

/**
 * 统计相关定时任务
 */

@Slf4j
@Component
public class StatisticTask {

    @Autowired
    private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;

    /**
     * 若商家币已过限售期，则更新其状态标志
     * 每日1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updateMerchantCurrencyRestrictStatus() {
        long sysTime = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        try {
            log.info("====若商家币已过限售期，则更新其状态标志====>>");
            List<MerchantAssertIssuance> items = merchantAssertStatisticsRepository.queryRestricted();
            items.forEach(x -> {
                cal.setTimeInMillis(x.getIssuingDate());
                long fixedIssuingDate = DateUtil.startOfTodDay(cal.getTime()).getTime();
                cal.setTimeInMillis(fixedIssuingDate);
                cal.add(DATE, x.getRestrictionPeriod());
                if (sysTime > cal.getTimeInMillis()) {
                    merchantAssertStatisticsRepository.update(new MerchantAssertStatistics(
                            x.getId(),
                            null,
                            null,
                            null,
                            BigDecimal.valueOf(YES.value()))
                    );
                }
            });
            log.info("====若商家币已过限售期，则更新其状态标志====<<");
        } catch (Exception e) {
            log.error("====若商家币已过限售期，则更新其状态标志====", e);
        }
    }

}
