package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.PlatformAssertSeller;
import com.yz.aac.opadmin.repository.domain.UserLevel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlatformAssertSellerRepository {

    String ALL_FIELDS = "id, name, support_alipay, support_wechat, support_bank_card, alipay_account, alipay_qr_code_path, wechat_account, wechat_qr_code_path, bank_card_number, total_sold_currency, total_sold_count, create_time, update_time";
    String QUERY_SELLERS = "<script>SELECT " + ALL_FIELDS + " FROM platform_assert_seller"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND name LIKE #{fixedName}</if>"
            + "<if test=\"accurateName != null and accurateName != ''\"> AND name = #{accurateName}</if>"
            + "</where>"
            + "ORDER BY total_sold_currency DESC"
            + "</script>";
    String STORE_SELLER = "INSERT INTO platform_assert_seller(" + ALL_FIELDS + ") VALUES(#{id}, #{name}, #{supportAlipay}, #{supportWechat}, #{supportBankCard}, #{alipayAccount}, #{alipayQrCodePath}, #{wechatAccount}, #{wechatQrCodePath}, #{bankCardNumber}, #{totalSoldCurrency}, #{totalSoldCount}, #{createTime}, #{updateTime})";
    String UPDATE_SELLER = "UPDATE platform_assert_seller SET name = #{name}, support_alipay = #{supportAlipay}, support_wechat = #{supportWechat}, support_bank_card = #{supportBankCard}, alipay_account = #{alipayAccount}, alipay_qr_code_path = #{alipayQrCodePath}, wechat_account = #{wechatAccount}, wechat_qr_code_path = #{wechatQrCodePath}, bank_card_number = #{bankCardNumber}, total_sold_currency = #{totalSoldCurrency}, total_sold_count = #{totalSoldCount}, create_time = #{createTime}, update_time = #{updateTime} WHERE id = #{id}";
    String DELETE_SELLER = "DELETE FROM platform_assert_seller WHERE id = #{id}";

    @Select(QUERY_SELLERS)
    List<PlatformAssertSeller> query(PlatformAssertSeller condition);

    @Insert(STORE_SELLER)
    void store(PlatformAssertSeller seller);

    @Update(UPDATE_SELLER)
    void update(PlatformAssertSeller seller);

    @Delete(DELETE_SELLER)
    void delete(Long id);
}
