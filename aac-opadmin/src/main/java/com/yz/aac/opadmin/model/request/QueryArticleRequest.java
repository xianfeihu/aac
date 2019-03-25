package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryArticleRequest {

    private String title;

    private PagingRequest paging;
}
