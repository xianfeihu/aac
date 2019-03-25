package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.repository.domian.LockMoneyTransaction;

/**
 * 临时记录当前交易信息
 *
 */

@Mapper
public interface LockMoneyTransactionRepository {

	String SAVE_LOCK_MONEY_TRANSACTION = "INSERT INTO lock_money_transaction(user_id, order_id, price, create_time) "
			+ "values(#{userId}, #{orderId}, #{price}, #{createTime})";
	
	String QUERY_LOCK_MONEY_TRANSACTION = "SELECT * FROM lock_money_transaction WHERE user_id = #{userId} AND order_id = #{orderId}  ORDER BY create_time DESC LIMIT 1";
	
	String DELETE_LOCK_MONEY_TRANSACTION = "DELETE FROM lock_money_transaction WHERE create_time < (unix_timestamp(NOW() - interval 10 MINUTE)*1000)";
	 
	@Insert(SAVE_LOCK_MONEY_TRANSACTION)
    int saveLockMoneyTransaction(LockMoneyTransaction lockMoneyTransaction);
	
	@Select(QUERY_LOCK_MONEY_TRANSACTION)
	LockMoneyTransaction getLockMoneyTransaction(@Param("userId") Long userId, @Param("orderId") Long orderId);
	
	/**
	 * 删除十分钟之前数据
	 * @return
	 */
	@Delete(DELETE_LOCK_MONEY_TRANSACTION)
	int deleteLockMoneyTransaction();
	
}
