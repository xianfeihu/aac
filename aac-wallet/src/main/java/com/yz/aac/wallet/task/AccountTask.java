package com.yz.aac.wallet.task;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yz.aac.wallet.repository.OrderReferenceNumberRepository;
import com.yz.aac.wallet.repository.UserBirthdayLoginFailRecordRepository;
import com.yz.aac.wallet.repository.UserSmsCodeRepository;

/**
 * 用户账号相关定时任务
 *
 */

@Slf4j
@Component
public class AccountTask {

	@Autowired
	private UserSmsCodeRepository userSmsCodeRepository;
	
	@Autowired
	private UserBirthdayLoginFailRecordRepository userBirthdayLoginFailRecordRepository;
	
	@Autowired
	private OrderReferenceNumberRepository orderReferenceNumberRepository;
	
	
	/**
	 * 1.清除短信验证码
	 * 2.交易参考号
	 * 每周星期天执行
	 */
    @Scheduled(cron = "0 0 0 ? * 1 ")
	private void ClearUserSmsCode(){
    	try{
    		userSmsCodeRepository.deleteSmsCodeRegular();
    		log.info("******执行自动清理短信验证码******");
    	}catch(Exception e){
    		log.error("******执行自动清理短信验证码异常******", e);
    	}
    	
    	try{
    		orderReferenceNumberRepository.deleteReferenceNumber();
    		log.info("******执行自动清理交易参考号******");
    	}catch(Exception e){
    		log.error("******执行自动清理交易参考号异常******", e);
    	}
    	
	}
    
    /**
     * 清除用户生日登录错误记录
     * 每月1号凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    private void ClearUserLoginErrorMes(){
    	userBirthdayLoginFailRecordRepository.deleteBirthdayLonginFailRecord();
    	log.info("******执行自动清理用户生日登录错误记录******");
    }

}
