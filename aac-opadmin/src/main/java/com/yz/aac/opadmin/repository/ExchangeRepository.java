package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.Exchange;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExchangeRepository {

    String ALL_FIELDS = "id, name, category, sub_category, customized, limit_in_month, activated, order_number";
    String QUERY = "<script>SELECT " + ALL_FIELDS + " FROM exchange"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "</where>"
            + " ORDER BY order_number"
            + "</script>";
    String UPDATE = "UPDATE exchange SET customized = #{customized}, limit_in_month = #{limitInMonth}, activated = #{activated} WHERE id = #{id}";

    @Select(QUERY)
    List<Exchange> query(Exchange exchange);

    @Update(UPDATE)
    void update(Exchange exchange);

}
