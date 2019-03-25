package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QueryFinancialLedgerRequest;
import com.yz.aac.opadmin.model.response.QueryFinancialLedgerResponse;

public interface FinancialService {

    /**
     * 查询进出帐
     */
    QueryFinancialLedgerResponse queryLedgers(QueryFinancialLedgerRequest request) throws Exception;


}
