package com.yz.aac.wallet.repository;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.yz.aac.wallet.repository.domain.MerchantAssertIssuanceAudit;

@Mapper
public interface MerchantAssertIssuanceAuditRepository {

    String BY_ISSUANCEID_AND_STATUS = "<script>SELECT * FROM merchant_assert_issuance_audit"
            + "<where>"
            + "issuance_id = #{issuanceId}"
            + "<if test=\"status != null and status.length &gt; 0\"> AND status in"
            + "<foreach collection=\"status\" open=\"(\" separator=\",\" close=\")\" item=\"sta\">"
            + "#{sta}"
            + "</foreach>"
            + "</if>"
            + "</where>"
            + "</script>";
    
    String UPDATE_STATUS_BY_USERID = "UPDATE merchant_assert_issuance_audit SET status = #{status} WHERE issuance_id = "
            + "("
            + "SELECT mai.id FROM  merchant_assert_issuance mai "
            + " LEFT JOIN merchant m on mai.merchant_id = m.id"
            + " LEFT JOIN user u on m.mobile_number = u.mobile_number"
            + " WHERE u.id = #{userId}"
            + ")";
    
    String QUERY_STATUS_BY_USERID = "SELECT status FROM merchant_assert_issuance_audit WHERE issuance_id = "
            + "("
            + "SELECT mai.id FROM  merchant_assert_issuance mai "
            + " LEFT JOIN merchant m on mai.merchant_id = m.id"
            + " LEFT JOIN user u on m.mobile_number = u.mobile_number"
            + " WHERE u.id = #{userId}"
            + ")";


    String INSERT_MERCHANT_ASSERT_ISSUANCE_AUDIT = " INSERT INTO merchant_assert_issuance_audit (issuance_id,`status`,request_time) VALUES (#{assertIssuanceId},#{status},#{time}) ";

    String QUERY_MERCHANT_ISSUE_BY_MERCHANT_ID = " SELECT maia.status,maia.audit_comment,maia.request_time,maia.audit_time FROM merchant_assert_issuance mai JOIN merchant_assert_issuance_audit maia ON maia.issuance_id=mai.id WHERE mai.merchant_id=#{merchantId} ";



    /**
     * 根据商家查找发币审批ID
     * @return
     */
    @Select(BY_ISSUANCEID_AND_STATUS)
    List<MerchantAssertIssuanceAudit> getByIssuanceIdAndStatus(@Param("issuanceId") Long issuanceId, @Param("status") Integer[] status);
    
    /**
     * 根据用户ID修改对应商户账号发币（押金）审核状态
     * @param userId 用户ID
     * @param status 审核状态
     */
    @Update(UPDATE_STATUS_BY_USERID)
    void updateStatusByUserId(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 根据用户ID获取对应商户账号发币（押金）审核状态
     * @param userId 用户ID
     * @return
     */
    @Select(QUERY_STATUS_BY_USERID)
    Integer getStatusByUserId(@Param("userId") Long userId);

    @Insert(INSERT_MERCHANT_ASSERT_ISSUANCE_AUDIT)
    void addMerchantAssertIssuanceAudit(@Param("assertIssuanceId") Long assertIssuanceId, @Param("status") Integer status, @Param("time") Long time);

    @Select(QUERY_MERCHANT_ISSUE_BY_MERCHANT_ID)
    MerchantAssertIssuanceAudit findMerchantIssueByMerchantId(@Param("merchantId") Long merchantId);
}
