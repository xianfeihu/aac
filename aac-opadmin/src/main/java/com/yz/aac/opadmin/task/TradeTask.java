package com.yz.aac.opadmin.task;

import com.yz.aac.opadmin.repository.PlatformAssertTradeRecordRepository;
import com.yz.aac.opadmin.repository.domain.PlatformAssertTradeRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 交易相关定时任务
 */

@Slf4j
@Component
public class TradeTask {

    @Autowired
    private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;

    /**
     * 更改“超过24小时未转账的平台币购买记录”状态为“取消”
     * 每日4点执行
     */
    @Scheduled(cron = "0 0 4 * * ?")
    private void cancelExpiredPlatformCurrencyRecords() {
        try {
            log.info("====更改“超过24小时未转账的平台币购买记录”状态为“取消”====>>");
            PlatformAssertTradeRecord param = new PlatformAssertTradeRecord();
            param.setTradeTime(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
            platformAssertTradeRecordRepository.updateExpiredRecords(param);
            log.info("====更改“超过24小时未转账的平台币购买记录”状态为“取消”====<<");
        } catch (Exception e) {
            log.error("====更改“超过24小时未转账的平台币购买记录”状态为“取消”====", e);
        }
    }

}
