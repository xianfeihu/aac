package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CreateUserLevelRequest {

    private String name;

    private Double matchCondition;

    private UploadRequest icon;
}
