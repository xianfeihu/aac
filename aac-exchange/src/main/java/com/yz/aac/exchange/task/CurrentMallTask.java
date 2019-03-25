package com.yz.aac.exchange.task;

import com.yz.aac.common.util.DateUtil;
import com.yz.aac.exchange.model.response.CurrencyMallIndexInfoForTaskResponse;
import com.yz.aac.exchange.repository.CurrencyMallRepository;
import com.yz.aac.exchange.repository.MerchantAssertTodayTradeRecordRepository;
import com.yz.aac.exchange.repository.MerchantCurrencyStatisticsRepository;
import com.yz.aac.exchange.repository.MerchantDividendRecordRepository;
import com.yz.aac.exchange.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.yz.aac.exchange.Constants.IssuanceAuditStatus.DEPOSIT_YES;
import static com.yz.aac.exchange.Constants.MerchantAssertLatestDataCountTypeEnum.*;
import static com.yz.aac.exchange.Constants.MerchantAssertStatisticsKey.SELL_REST;
import static com.yz.aac.exchange.Constants.MerchantCurrencyStatisticsAssetTypeEnum.DYNAMIC_STOCK;
import static com.yz.aac.exchange.Constants.MerchantCurrencyStatisticsAssetTypeEnum.FREEZE_STOCK;
import static com.yz.aac.exchange.Constants.UserRreezeReason.SALE_OF_BILLS;

/**
 * 币商城相关定时任务
 *
 */

@Slf4j
@Component
public class CurrentMallTask {

	@Autowired
	private CurrencyMallRepository currencyMallRepository;

	@Autowired
	private MerchantAssertTodayTradeRecordRepository merchantAssertTodayTradeRecordRepository;

	@Autowired
	private MerchantCurrencyStatisticsRepository currencyStatisticsRepository;

	@Autowired
	private MerchantDividendRecordRepository merchantDividendRecordRepository;

	@Autowired
	private MerchantService merchantServiceImpl;

	/**
	 * 更新币商城首页信息
	 * 10s一次
	 */
	@Scheduled(cron="0/10 * * * * ?")
	@Transactional(rollbackFor = Exception.class)
	public void updateCurrencyMallIndexInfoForSecond() {
		try {
			this.currencyMallRepository.deleteCurrencyMallIndexInfo();
			this.currencyMallRepository.updateCurrencyMallIndexInfo(DateUtils.MILLIS_PER_DAY, DateUtil.startOfTodDay(new Date()).getTime(), DAY.code(), DEPOSIT_YES.code(),System.currentTimeMillis());
		} catch (Exception e) {
			log.error("币商城首页数据更新失败",e);
		}
	}

	/**
	 * 每日零晨两点定时任务
	 */
	@Scheduled(cron="0 0 2 * * ? ")
	@Transactional(rollbackFor = Exception.class)
	public void timeTaskForTwoPoints() {
		/**
		 * 分红执行
		 */
		try {
			this.merchantServiceImpl.updateMerchantDividendRecord();
			log.info("******分红任务执行成功******");
		} catch (Exception e) {
			log.error("分红执行失败",e);
		}
	}

	/**
	 * 每日零晨定时任务
	 */
	@Scheduled(cron="0 0 0 * * ? ")
	@Transactional(rollbackFor = Exception.class)
	public void timeTaskForToday() {
		Long startOfTodDay = DateUtil.startOfTodDay(new Date()).getTime();
		/**
		 * 昨日商户订单信息清除
		 */
		try {
			this.merchantAssertTodayTradeRecordRepository.clearYesterdayTradeRecord(startOfTodDay);
			log.info("******昨日商户订单信息清除成功******");
		} catch (Exception e) {
			log.error("昨日订单信息清除失败！！！",e);
		}
		/**
		 * 商户货币信息统计部分
		 */
		try {
			// 商户货币统计-活跃存量 (创建时间为当日凌晨)
			this.currencyStatisticsRepository.addMerchantActiveStockForDay(DYNAMIC_STOCK.code(),startOfTodDay,SELL_REST.name(),SALE_OF_BILLS.code(),DEPOSIT_YES.code());
			// 商户货币统计-冻结存量 (创建时间为当日凌晨)
			this.currencyStatisticsRepository.addMerchantOrderFreezeStockForDay(FREEZE_STOCK.code(),startOfTodDay,DEPOSIT_YES.code());
			log.info("******商户货币信息统计(活跃、冻结存量)执行成功******");
		} catch (Exception e) {
			log.error("商户货币信息统计失败！！！",e);
		}

		/**
		 * 清除商户分红的缓存表信息
		 *  TRUNCATE currency_mall_index_info
		 */
		try {
			this.merchantDividendRecordRepository.truncateTemp();
			log.info("******清除商户分红的缓存表执行成功******");
		} catch (Exception e) {
			log.error("清除商户分红的缓存表信息失败！！！",e);
		}
	}

	/**
	 * 每周一定时任务
	 */
	@Scheduled(cron="0 0 0 ? * 2")
	@Transactional(rollbackFor = Exception.class)
	public void timeTaskForMonday() {
		try {
			Long lastMondayBeginTime = DateUtil.lastMonday().getTime();
			Long startOfTodDay = DateUtil.startOfTodDay(new Date()).getTime();

			/**
			 * 统计上周交易信息---创建时间为上周末凌晨
			 */
			this.currencyMallRepository.updateCurrencyMallIndexInfoForTask(new CurrencyMallIndexInfoForTaskResponse(DAY.code(),WEEK.code(),lastMondayBeginTime,startOfTodDay,startOfTodDay-DateUtils.MILLIS_PER_DAY));
			log.info("上周K线图信息统计成功");

		} catch (Exception e) {
			log.error("每周一定时任务执行失败！！！",e);
		}
	}

	/**
	 * 每月一号定时任务
	 */
	@Scheduled(cron="0 0 0 1 * ? ")
	@Transactional(rollbackFor = Exception.class)
	public void timeTaskForEarlyMonth() {
		Long startOfTodDay = DateUtil.startOfTodDay(new Date()).getTime();

		/**
		 * 统计上月交易信息---创建时间为上月最后一天凌晨
		 */
		try {
			Long lastMonthBeginTime = DateUtil.lastMonthBeginTime().getTime();
			this.currencyMallRepository.updateCurrencyMallIndexInfoForTask(new CurrencyMallIndexInfoForTaskResponse(WEEK.code(),MONTH.code(),lastMonthBeginTime, startOfTodDay,startOfTodDay-DateUtils.MILLIS_PER_DAY));
			log.info("上月K线图信息统计成功");
		} catch (Exception e) {
			log.error("上月K线图信息统计失败！！！",e);
		}


		/**
		 * 商户货币统计清除上周信息
		 */
		try {
			Long mondayTime = DateUtil.mondayTime().getTime();
			// 删除上月信息
			this.currencyStatisticsRepository.deletelastMonthMerchantStatistics(mondayTime);
			log.info("******商户货币统计清除上周信息执行成功******");
		} catch (Exception e) {
			log.error("商户货币统计清除上周信息失败！！！",e);
		}
	}
}
