package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.PlatformAssertSellingOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlatformAssertSellingOrderRepository {

    String ALL_FIELDS = "o.id, o.seller_id, s.name AS seller_name, o.available_trade_amount, o.min_amount_limit, o.max_amount_limit, o.rmb_price, o.remark, o.create_time, o.update_time";
    String QUERY_ORDERS = "<script>SELECT " + ALL_FIELDS + " FROM platform_assert_selling_order o INNER JOIN platform_assert_seller s ON o.seller_id = s.id"
            + "<where>"
            + "<if test=\"id != null\"> AND o.id = #{id}</if>"
            + "<if test=\"sellerId != null\"> AND o.seller_id = #{sellerId}</if>"
            + "<if test=\"sellerName != null and sellerName != ''\"><bind name=\"fixedName\" value=\"'%' + sellerName + '%'\" /> AND s.name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY o.create_time DESC"
            + "</script>";
    String STORE_ORDER = "INSERT INTO platform_assert_selling_order(id, seller_id, available_trade_amount, min_amount_limit, max_amount_limit, rmb_price, remark, create_time, update_time) VALUES(#{id}, #{sellerId}, #{availableTradeAmount}, #{minAmountLimit}, #{maxAmountLimit}, #{rmbPrice}, #{remark}, #{createTime}, #{updateTime})";
    String UPDATE_ORDER = "UPDATE platform_assert_selling_order SET seller_id = #{sellerId}, available_trade_amount = #{availableTradeAmount}, min_amount_limit = #{minAmountLimit}, max_amount_limit = #{maxAmountLimit}, rmb_price = #{rmbPrice}, remark = #{remark}, update_time= #{updateTime} WHERE id = #{id}";

    @Select(QUERY_ORDERS)
    List<PlatformAssertSellingOrder> query(PlatformAssertSellingOrder condition);

    @Insert(STORE_ORDER)
    void store(PlatformAssertSellingOrder order);

    @Update(UPDATE_ORDER)
    void update(PlatformAssertSellingOrder order);
}
