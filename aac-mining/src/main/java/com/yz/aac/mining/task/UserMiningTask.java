package com.yz.aac.mining.task;

import com.yz.aac.common.Constants;
import com.yz.aac.mining.Constants.AccountStatus;
import com.yz.aac.mining.Constants.MiningBonusTypeEnum;
import com.yz.aac.mining.Constants.PlatformAssertStatistics;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.IncreaseStrategy;
import com.yz.aac.mining.repository.domian.User;
import com.yz.aac.mining.repository.domian.UserNaturalGrowthRecord;
import com.yz.aac.mining.util.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户挖矿相关定时任务
 *
 */

@Slf4j
@Component
public class UserMiningTask {

	private final static int ALGORITHM_DAY_NUM = 1095;//自然增长能力算法天数
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IncreaseStrategyRepository increaseStrategyRepository;

	@Autowired
	private UserNaturalGrowthRecordRepository userNaturalGrowthRecordRepository;
	
	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;
	
	@Autowired
	private PlatformAssertStatisticsRepository platformAssertStatisticsRepository;

	@Autowired
	private UserAssertRepository userAssertRepository;
	
    /**
     * 用户账号每日增长AAB能量算法
     * 每天凌晨一点半执行
     * 
     * 自然增长能量算法：
     * 每日自然增长AAB=（用户昨日增长元力*【】
     	+昨日广告有效点击或提交信息总数量*【】
        +昨日总AAB数量*【】）
        /（自然增长数量/1095天）。
     * 
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void AddUserNaturalGrowth(){
    	log.info("******用户账号每日增长AAB能量算法==>BEFORE******");
		
    	//当前系统用户所有账号
    	List<User> userList = userRepository.getUserAll(AccountStatus.ENABLE.code());
    	
        int randomNumber = (int) Math.round(Math.random()*(35-10)+10);  
        
    	for (User u : userList) {
    		try{
				IncreaseStrategy strategy = increaseStrategyRepository.getByUserId(u.getId());
				//当前用户增长算法
				BigDecimal todayNaturalGrowth = (userMiningRecordRepository.getRecordYesterday(u.getId(), MiningBonusTypeEnum.POWER_POINT.code()).multiply(BigDecimal.valueOf(strategy.getIncreasedPowerPoint()))
						.add(BigDecimal.valueOf(0).multiply(BigDecimal.valueOf(strategy.getConsumedAd())))
						.add(userAssertRepository.getUserAvailableFundsByCurrencySymbol(u.getId(), Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value())).multiply(BigDecimal.valueOf(strategy.getPlatformCurrency()))
				);

				BigDecimal growthNum = platformAssertStatisticsRepository.getPlatformAssertStatistics(PlatformAssertStatistics.ACTIVE_INCREASE.name())
						.divide(BigDecimal.valueOf(ALGORITHM_DAY_NUM), 2, BigDecimal.ROUND_HALF_DOWN);

				todayNaturalGrowth = todayNaturalGrowth.divide(growthNum, 2, BigDecimal.ROUND_HALF_DOWN);

				//随机分配能量
				double[] randAry = RedPacketUtil.allocateRedPacket(randomNumber, todayNaturalGrowth.doubleValue());

				if (null != randAry && randAry.length > 0) {
					for (double d : randAry) {
						//添加记录
						userNaturalGrowthRecordRepository.saveUserNaturalGrowthRecord(
								new UserNaturalGrowthRecord(u.getId(), BigDecimal.valueOf(d), System.currentTimeMillis()));
					}
				}
			} catch (Exception e) {
				log.error("******用户ID【" + u.getId() + "】每日增长算法错误！******", e);
			}
    	}
		log.info("******用户账号每日增长AAB能量算法==>END******");
    }
    
    /**
     * 清理过期用户每日增长能量
     * 每周五凌晨两点执行
     */
    @Scheduled(cron = "0 0 2 ? * FRI")
    private void DelateUserNaturalGrowth(){
    	log.info("******清理过期用户每日增长能量==>BEFORE******");
    	userNaturalGrowthRecordRepository.deleteNaturalGrowth();
		log.info("******清理过期用户每日增长能量==>END******");
    }

}
