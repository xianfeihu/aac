package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryAlgorithmResponse;

import java.util.List;

public interface AlgorithmService {

    /**
     * 查询算法
     */
    QueryAlgorithmResponse queryAlgorithms(QueryAlgorithmRequest request) throws Exception;

    /**
     * 创建算法
     */
    void createAlgorithm(CreateAlgorithmRequest request) throws Exception;

    /**
     * 更新算法
     */
    void updateAlgorithm(UpdateAlgorithmRequest request) throws Exception;

    /**
     * 删除算法
     */
    void deleteAlgorithm(DeleteAlgorithmRequest request) throws Exception;

    /**
     * 设置默认算法
     */
    void applyDefaultAlgorithm(ApplyDefaultAlgorithmRequest request) throws Exception;
}
