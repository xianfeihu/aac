package com.yz.aac.wallet.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.model.response.CoinDetailsMsgResponse;

@Mapper
public interface UserAssertFreezeRepository {

    String QUERY_ASSERT_FREEZE = " SELECT uaf.currency_symbol,uaf.amount,uaf.reason*1 reason,mald.recent_price platform_price FROM user_assert_freeze uaf LEFT JOIN (SELECT currency_symbol,recent_price FROM merchant_assert_latest_data WHERE create_time>#{beginTime}) mald USING (currency_symbol) WHERE uaf.user_id=#{userId} ";

    @Select(QUERY_ASSERT_FREEZE)
    List<CoinDetailsMsgResponse> getFreezeRepositoryByUserId(@Param("userId") Long userId,@Param("beginTime") Long beginTime);

}
