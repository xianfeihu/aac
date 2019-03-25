package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.CreatePlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.request.DeletePlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.request.QueryPlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.request.UpdatePlatformAssertSellerRequest;
import com.yz.aac.opadmin.model.response.QueryPlatformAssertSellerResponse;

public interface PlatformAssertSellerService {

    /**
     * 查询挂单人员
     */
    QueryPlatformAssertSellerResponse queryPlatformAssertSellers(QueryPlatformAssertSellerRequest request) throws Exception;

    /**
     * 创建挂单人员
     */
    void createPlatformAssertSeller(CreatePlatformAssertSellerRequest request) throws Exception;

    /**
     * 更新挂单人员
     */
    void updatePlatformAssertSeller(UpdatePlatformAssertSellerRequest request) throws Exception;

    /**
     * 删除挂单人员
     */
    void deletePlatformAssertSeller(DeletePlatformAssertSellerRequest request) throws Exception;
}
