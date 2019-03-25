package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateArticleRequest {

    private Long loginUserId;

    private Long categoryId;

    private String title;

    private String[] texts;

    private UploadRequest[] files;

    private Integer[] indexes;

}
