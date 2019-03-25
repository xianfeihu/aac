package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.ExchangeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExchangeItemRepository {

    String QUERY_EXCHANGE_ITEM = "SELECT * FROM exchange_item WHERE id = #{itemId}";

    String QUERY_EXCHANGE_ITEM_BY_EXCHANGEID = "SELECT * FROM exchange_item WHERE exchange_id = #{exchangeId} order by order_number";

    @Select(QUERY_EXCHANGE_ITEM)
    ExchangeItem getExchangeItem(@Param("itemId") Long itemId);

    @Select(QUERY_EXCHANGE_ITEM_BY_EXCHANGEID)
    List<ExchangeItem> getExchangeItemByExchangeId(@Param("exchangeId") Long exchangeId);
}
