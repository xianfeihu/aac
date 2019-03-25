package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryInvitationParticipatorRequest {

    private String name;

    private String code;

    private Long id;

    private PagingRequest paging;

}
