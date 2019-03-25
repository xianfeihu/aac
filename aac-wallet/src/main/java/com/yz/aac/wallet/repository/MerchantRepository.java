package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.Merchant;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MerchantRepository {

    String QUERY_MERCHANT_BY_MOBILE = "SELECT * FROM merchant WHERE mobile_number = #{mobile}";

    String QUERY_MERCHANT_BY_ID = "SELECT id,`name`,merchant_name,gender,id_number,mobile_number,create_time FROM merchant WHERE id = #{merchantId}";

    String QUERY_MERCHANT_BY_MOBILE_OR_ID_NUMBER = "SELECT id,name,merchant_name,gender,id_number,mobile_number,create_time FROM merchant WHERE mobile_number=#{mobileNumber} or id_number=#{idNumber} LIMIT 1;";

    String INSERT_MERCHANT = " INSERT INTO merchant (`name`,merchant_name,merchant_visit_url,gender,id_number,mobile_number,create_time) VALUES (#{name},#{merchantName},#{merchantVisitUrl},#{gender},#{idNumber},#{mobileNumber},#{createTime}) ";

    String QUERY_MERCHANT_BY_IDNUMBER = "select * from merchant where id_number = #{idNumber}";

    String UPDATE_MERCHANT_BY_ID = "<script>UPDATE `merchant` SET " +
            "<trim suffixOverrides=\",\">" +
            "<if test=\"merchantName != null and merchantName!=''\">" +
            "   `merchant_name` = #{merchantName}," +
            "</if>" +
            "<if test=\"merchantVisitUrl != null and merchantVisitUrl!=''\">" +
            "   `merchant_visit_url` = #{merchantVisitUrl}," +
            "</if>" +
            "</trim>" +
            " WHERE `id` = #{id}</script>";

    String QUERY_QRCODE_BY_CURRENCYSYMBOL = " SELECT m.merchant_visit_url FROM merchant_assert_issuance mai LEFT JOIN merchant m ON m.id=mai.merchant_id WHERE currency_symbol=#{currencySymbol} ";

    @Select(QUERY_MERCHANT_BY_MOBILE)
    Merchant getMerchantByMobile(@Param("mobile") Long mobile);

    @Select(QUERY_MERCHANT_BY_MOBILE_OR_ID_NUMBER)
    Merchant getMerchantByMobileOrIdNumber(@Param("mobileNumber") Long mobileNumber, @Param("idNumber") String idNumber);

    @Insert(INSERT_MERCHANT)
    @Options(useGeneratedKeys = true)
    Long addMerchant(Merchant merchant);

    @Select(QUERY_MERCHANT_BY_ID)
    Merchant getMerchantById(@Param("merchantId") Long merchantId);
    
    @Select(QUERY_MERCHANT_BY_IDNUMBER)
    Merchant queryMerchantByIdNumber(@Param("idNumber") String idNumber);

    @Update(UPDATE_MERCHANT_BY_ID)
    Integer updateMerchantById(@Param("merchantName") String merchantName, @Param("merchantVisitUrl") String merchantVisitUrl, @Param("id") Long id);

    @Select(QUERY_QRCODE_BY_CURRENCYSYMBOL)
    String getMchQrCode(@Param("currencySymbol") String currencySymbol);

}
