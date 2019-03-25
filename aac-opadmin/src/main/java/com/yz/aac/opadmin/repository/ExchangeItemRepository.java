package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.Exchange;
import com.yz.aac.opadmin.repository.domain.ExchangeItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExchangeItemRepository {

    String ALL_FIELDS = "id, exchange_id, rmb_amount, platform_amount, order_number";
    String STORE = "INSERT INTO exchange_item(" + ALL_FIELDS + ") VALUES(#{id}, #{exchangeId}, #{rmbAmount}, #{platformAmount}, #{orderNumber})";
    String DELETE = "<script>DELETE FROM exchange_item"
            + "<where>"
            + "<if test=\"exchangeId != null\"> AND exchange_id = #{exchangeId}</if>"
            + "</where>"
            + "</script>";
    String QUERY = "<script>SELECT " + ALL_FIELDS + " FROM exchange_item"
            + "<where>"
            + "<if test=\"exchangeId != null\"> AND exchange_id = #{exchangeId}</if>"
            + "</where>"
            + " ORDER BY order_number"
            + "</script>";

    @Insert(STORE)
    void store(ExchangeItem item);

    @Delete(DELETE)
    void delete(ExchangeItem item);

    @Select(QUERY)
    List<ExchangeItem> query(ExchangeItem condition);

}
