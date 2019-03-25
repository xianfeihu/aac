package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UpdateUserLevelRequest {

    private Long id;

    private String name;

    private Double matchCondition;

    private Boolean updateIcon;

    private UploadRequest icon;
}
