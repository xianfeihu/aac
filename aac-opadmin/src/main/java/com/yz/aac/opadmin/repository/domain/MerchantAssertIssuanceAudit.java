package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAssertIssuanceAudit {

    private Long id;

    private Long issuanceId;

    private Integer status;

    private String auditComment;

    private Long requestTime;

    private Long auditTime;

}
