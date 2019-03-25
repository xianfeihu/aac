package com.yz.aac.opadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePlatformAssertSellerRequest {

    private Long id;

    private Long loginId;
}
