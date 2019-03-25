package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleElement {

    private Long id;

    private Long articleId;

    private Integer elementType;

    private String elementContent;

    private Integer orderNumber;

}
