package com.yz.aac.opadmin.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private Long id;

    private Long categoryId;

    private String title;

    private Long authorId;

    private Integer authorType;

    private Long createTime;

}
