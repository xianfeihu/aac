package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.Exchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExchangeRepository {

    String QUERY_EXCHANGE_BY_ACTIVATED = "SELECT * FROM exchange WHERE activated = #{activated}";

    String QUERY_EXCHANGE_BY_ID = "SELECT * FROM exchange WHERE id = #{id}";

    String QUERY_EXCHANGE_BY_CATEGORY = "SELECT * FROM exchange WHERE category = #{category}";

    @Select(QUERY_EXCHANGE_BY_ACTIVATED)
    List<Exchange> getExchangeByActivated(@Param("activated") Integer activated);

    @Select(QUERY_EXCHANGE_BY_ID)
    Exchange getExchangeById(@Param("id") Long id);

    @Select(QUERY_EXCHANGE_BY_CATEGORY)
    List<Exchange> getExchangeBycategory(@Param("category") Integer category);

}
