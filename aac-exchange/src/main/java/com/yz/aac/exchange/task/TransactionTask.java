package com.yz.aac.exchange.task;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yz.aac.exchange.repository.LockMoneyTransactionRepository;
import com.yz.aac.exchange.repository.OrderReferenceNumberRepository;
import com.yz.aac.exchange.repository.UserPaymentErrorRecordRepository;

/**
 * 交易相关定时任务
 *
 */

@Slf4j
@Component
public class TransactionTask {

	@Autowired
	private UserPaymentErrorRecordRepository userPaymentErrorRecordRepository;
	
	@Autowired
	private LockMoneyTransactionRepository lockMoneyTransactionRepository;
	
	@Autowired
	private OrderReferenceNumberRepository orderReferenceNumberRepository;
	
	
	/**
	 * 清除交易数据锁
	 * 每周凌晨一点执行
	 */
    @Scheduled(cron = "0 0 1 * * ?")
	private void ClearUserSmsCode(){
    	log.info("******执行自动清理短信验证码==>BEFORE******");
		lockMoneyTransactionRepository.deleteLockMoneyTransaction();
		log.info("******执行自动清理短信验证码==>END******");
		
	}
    
}
