package com.yz.aac.exchange.repository;

import java.math.BigDecimal;
import java.util.List;

import com.yz.aac.exchange.model.response.CurrencyTradingListResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.exchange.model.response.MerchantActiveStockResponse;
import com.yz.aac.exchange.repository.domian.UserOrder;

@Mapper
public interface UserOrderRepository {

	String SAVE_USER_ORDER = "INSERT INTO user_order(user_id, original_limit, available_trade_amount, currency_symbol, aab_price, "
			+ "remark, type, status, create_time) values(#{userId}, #{originalLimit} ,#{availableTradeAmount}, #{currencySymbol}, "
			+ "#{aabPrice}, #{remark}, #{type}, #{status}, #{createTime})";
    
	String QUERY_USER_ORDER_LIST = "SELECT u.name as personName,uo.available_trade_amount as number,uo.aab_price as price,uo.id as orderId,uo.currency_symbol as currencySymbol "
			+ " FROM user_order uo left join user u on u.id = uo.user_id "
			+ "WHERE currency_symbol = #{currencySymbol} AND type =#{type} AND status = #{status}"
			+ " ORDER BY uo.aab_price ${orderBy}";
	
	String QUERY_USER_ORDER_INFO_BY_ID = "SELECT * FROM user_order WHERE id = #{id}";
	
	String QUERY_MY_ORDER_LIST = "<script>SELECT u.name as personName, uo.available_trade_amount as number, uo.aab_price as price, uo.id as orderId, uo.currency_symbol as currencySymbol,uo.type as type "
			+ "FROM user_order uo "
			+ "LEFT JOIN user u on u.id = uo.user_id "
			+ "<where>"
            + "user_id = #{userId}"
            + "<if test=\"status != null and status.length &gt; 0\">  AND status in"
            + "<foreach collection=\"status\" open=\"(\" separator=\",\" close=\")\" item=\"sta\">"
            + "#{sta}"
            + "</foreach>"
            + "</if>"
            + "</where>"
            + "</script>";
	
	String UPDATE_USER_ORDER_BY_ID = "UPDATE user_order SET available_trade_amount = #{availableTradeAmount}, aab_price = #{aabPrice}, remark = #{remark}, update_time = unix_timestamp(NOW())*1000 WHERE id = #{id}";
	
	String UPDATE_USER_ORDER_STATUS_BY_ID = "UPDATE user_order SET status = #{status} WHERE id = #{id}";

	String QUERY_MAX_AMOUNT_ORDER_ID = " SELECT id FROM user_order WHERE currency_symbol = #{currencySymbol} AND type = #{type} AND `status` = #{status} AND `user_id`!=#{userId} ORDER BY available_trade_amount DESC ,aab_price ${order} LIMIT 1";

	String UPDATE_USER_ORDER_AMOUNT_BY_ID = "UPDATE user_order SET available_trade_amount = available_trade_amount + #{availableTradeAmount} WHERE id = #{id}";
	
	String QUERY_USER_ORDER_BY_CURRENCYSYMBOL = "select * from user_order where currency_symbol = #{currencySymbol} ORDER BY create_time ASC LIMIT 1";
	
	@Insert(SAVE_USER_ORDER)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserOrder(UserOrder userOrder);
	
	@Select(QUERY_USER_ORDER_LIST)
	List<CurrencyTradingListResponse> getUserOrderList(@Param("currencySymbol") String currencySymbol, @Param("type") Integer type, @Param("status") Integer status, @Param("orderBy") String orderBy);
	
	@Select(QUERY_MY_ORDER_LIST)
	List<CurrencyTradingListResponse> getMyOrderList(@Param("userId") Long userId, @Param("status") Integer[] status);
	
	@Select(QUERY_USER_ORDER_INFO_BY_ID)
	UserOrder getUserOrderInfoById(@Param("id") Long id);
	
	@Update(UPDATE_USER_ORDER_BY_ID)
	void updateUserOrderById(@Param("id") Long id, @Param("availableTradeAmount") BigDecimal availableTradeAmount, 
			@Param("aabPrice") BigDecimal aabPrice, @Param("remark") String remark);
	
	@Update(UPDATE_USER_ORDER_STATUS_BY_ID)
	void updateUserOrderStatusById(@Param("id") Long id, @Param("status") Integer status);

	@Select(QUERY_MAX_AMOUNT_ORDER_ID)
	Long getMaxAmountOrder(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol,@Param("status") Integer status, @Param("type") Integer type, @Param("order") String order);
	
	/**
	 * 修改挂单出售额度
	 * @param id 挂单ID
	 * @param availableTradeAmount 出售额度（区分正负号）
	 */
	@Update(UPDATE_USER_ORDER_AMOUNT_BY_ID)
	void updateUserOrderAmountById(@Param("id") Long id, @Param("availableTradeAmount") BigDecimal availableTradeAmount);
	
	/**
	 * 用户第一笔挂单
	 * @param userId
	 * @return
	 */
	@Select(QUERY_USER_ORDER_BY_CURRENCYSYMBOL)
	UserOrder getUserOrderByCurrencySymbol(@Param("currencySymbol") String currencySymbol);
	
}
