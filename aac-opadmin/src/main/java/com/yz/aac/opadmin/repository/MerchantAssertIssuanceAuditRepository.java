package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuanceAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MerchantAssertIssuanceAuditRepository {

    String ALL_FIELDS = "id, issuance_id, status, audit_comment, request_time, audit_time";
    String QUERY_AUDITS = "<script>SELECT " + ALL_FIELDS + " FROM merchant_assert_issuance_audit"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "</where>"
            + "</script>";
    String UPDATE_AUDIT = "UPDATE merchant_assert_issuance_audit SET issuance_id = #{issuanceId}, status = #{status}, audit_comment = #{auditComment}, request_time = #{requestTime}, audit_time = #{auditTime} WHERE id = #{id}";

    @Select(QUERY_AUDITS)
    List<MerchantAssertIssuanceAudit> query(MerchantAssertIssuanceAudit audit);

    @Update(UPDATE_AUDIT)
    void update(MerchantAssertIssuanceAudit audit);
}
