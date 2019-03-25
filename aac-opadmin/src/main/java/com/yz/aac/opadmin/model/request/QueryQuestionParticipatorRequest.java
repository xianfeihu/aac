package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryQuestionParticipatorRequest {

    private String name;

    private Integer minCorrect;

    private Integer maxCorrect;

    private PagingRequest paging;

}
