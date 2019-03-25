package com.yz.aac.exchange.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.repository.domian.Merchant;

@Mapper
public interface MerchantRepository {

    String QUERY_ALL_CURRENCY = "<script>select currencySymbol from ("
    		+ " select tableA.currency_symbol as currencySymbol, max(tableA.request_time) as currency,"
    		+ "<if test=\"userId != null\"> "
    		+ " max(tableA.assertId) as assertId,"
            + "</if>"
    		+ " count(matr.id) as transactionCount"
    		+ " from ("
    		+ " select mai.currency_symbol,maia.request_time"
    		+ "<if test=\"userId != null\"> "
    		+ " ,case when ua.id is not null then 1 else 0 end assertId"
            + "</if>"
    		+ " from merchant m"
    		+ " left join merchant_assert_issuance mai on mai.merchant_id = m.id"
    		+ " left join merchant_assert_issuance_audit maia on maia.issuance_id = mai.id"
    		+ "<if test=\"userId != null\"> "
    		+ " left join user_assert ua on (ua.currency_symbol = mai.currency_symbol  AND ua.user_id = #{userId})"
            + "</if>"
            + "<where>"
            + "<if test=\"status != null and status.length &gt; 0\"> AND maia.status in "
            + "<foreach collection=\"status\" open=\"(\" separator=\",\" close=\")\" item=\"sta\">"
            + "#{sta}"
            + "</foreach>"
            + "</if>"
            + "</where>"
            + " ) as tableA"
            + " left join merchant_assert_trade_record matr on matr.currency_symbol = tableA.currency_symbol  "
            + "<if test=\"tradeTypes != null and tradeTypes.length &gt; 0\"> AND matr.trade_type in "
            + "<foreach collection=\"tradeTypes\" open=\"(\" separator=\",\" close=\")\" item=\"ty\">"
            + "#{ty}"
            + "</foreach>"
            + "</if>"
            + "<if test=\"userId != null\"> "
            + " AND matr.initiator_id = #{userId}"
            + "</if>"
            + " group by tableA.currency_symbol"
            + " ) as tableB "
            + " order by "
            + "<if test=\"userId != null\"> "
            + "assertId desc,"
            + "</if>"
            + "transactionCount desc,currency desc,currencySymbol desc"
            + "</script>";
    

    String QUERY_MERCHANT_BY_MOBILE = "SELECT * FROM merchant WHERE mobile_number = #{mobile}";
    
    String QUERY_QRCODE_BY_CURRENCYSYMBOL = "SELECT m.* FROM merchant_assert_issuance mai LEFT JOIN merchant m ON m.id=mai.merchant_id WHERE currency_symbol=#{currencySymbol}";
    
    @Select(QUERY_ALL_CURRENCY)
    List<String> getCurrencyList(@Param("userId") Long userId, @Param("status") Integer[] status, @Param("tradeTypes") Integer[] tradeTypes);
 
    @Select(QUERY_MERCHANT_BY_MOBILE)
    Merchant getMerchantByMobile(@Param("mobile") Long mobile);
    
    @Select(QUERY_QRCODE_BY_CURRENCYSYMBOL)
    Merchant getMerchantByCurrencySymbol(@Param("currencySymbol") String currencySymbol);
    
}
