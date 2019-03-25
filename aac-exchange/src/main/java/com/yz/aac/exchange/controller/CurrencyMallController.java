package com.yz.aac.exchange.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.exchange.model.request.CandlestickChartMsgRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.repository.domian.MerchantAssertTodayTradeRecord;
import com.yz.aac.exchange.service.CurrencyMallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.exchange.Constants.Misc.K_CHART_PATH;

@Slf4j
@RestController
@Api(tags = "数字币商城")
@RequestMapping(value = "/mall")
public class CurrencyMallController extends BaseController {

    @Autowired
    private CurrencyMallService currencyMallServiceImpl;

    @GetMapping("/kChartPath")
    @ApiOperation(value = "动态返回K线图地址信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "basePath", value = "仅传域名（www.xxx.com）", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    })
    public RootResponse<String> getKChartPath(String basePath, String currencySymbol) throws Exception {
        this.validateRequired("basePath", basePath);
        return buildSuccess(basePath.trim() + K_CHART_PATH.value() + currencySymbol);
    }

    @GetMapping("/getIndexInfo")
    @ApiOperation("首页相关信息")
    public RootResponse<List<CurrencyMallIndexInfoResponse>> getIndexInfo() throws Exception {
        return buildSuccess(this.currencyMallServiceImpl.getIndexInfo());
    }

    @GetMapping("/searchIndexInfo")
    @ApiOperation("按币种类搜索相关信息")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    public RootResponse<List<CurrencyMallIndexInfoResponse>> searchIndexInfo(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol", currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.searchIndexInfo(currencySymbol));
    }

    @GetMapping("/mallTradInfo")
    @ApiOperation("交易所买卖单信息")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    public RootResponse<List<MerchantAssertTodayTradeRecord>> exchangeTradInfo(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol",currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.exchangeTradInfo(currencySymbol));
    }

    @GetMapping("/candlestickChartMsgForNow")
    @ApiOperation("获取 K线图(实时数据)")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    public RootResponse<List<MerchantAssertTodayTradeRecord>> candlestickChartMsgForNow(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol",currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.candlestickChartMsgForNow(currencySymbol));
    }

    @GetMapping("/candlestickChartMsg")
    @ApiOperation("获取 K线图(日、周、月)")
    public RootResponse<List<CandlestickChartMsgResponse>> candlestickChartMsg(CandlestickChartMsgRequest request) throws Exception {
        this.validateRequired("currencySymbol",request.getCurrencySymbol());
        this.validateIntRange("type", request.getType(),1,3);
        /*this.validateRequired("beginTime",request.getBeginTime());
        this.validateRequired("endTime",request.getEndTime());
        if (request.getType().equals(DAY.code())) {
            // 2月限制
            if (request.getEndTime()>System.currentTimeMillis() || request.getBeginTime()<System.currentTimeMillis()-(DateUtil.MILLIS_PER_DAY * Integer.valueOf(TWO_MONTHS_FOR_DAY.value()))) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "查询时间超出范围");
            }
        } else if (request.getType().equals(WEEK.code())) {
            // 12月限制
            if (request.getEndTime()>System.currentTimeMillis() || request.getBeginTime()<System.currentTimeMillis()-(DateUtil.MILLIS_PER_DAY * Integer.valueOf(YEAR_FOR_DAY.value()))) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "查询时间超出范围");
            }
        }*/
        return buildSuccess(this.currencyMallServiceImpl.candlestickChartMsg(request));
    }

    @GetMapping("/candlestickChartBaseMsg")
    @ApiOperation("获取 K线图-基础信息")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    public RootResponse<CandlestickChartBaseMsgResponse> candlestickChartBaseMsg(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol", currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.candlestickChartBaseMsg(currencySymbol));
    }

    @GetMapping("/currencyIntroduction")
    @ApiOperation("获取 K线图-币简介")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型", required = true, dataType = "String")
    public RootResponse<CurrencyIntroductionResponse> currencyIntroduction(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol", currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.currencyIntroduction(currencySymbol));
    }

    @GetMapping("/buyCurrencyBySymbol")
    @ApiOperation("购买该商户币")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型 -> 状态码MSG_4002 跳转（我要挂买单）", required = true, dataType = "String")
    public RootResponse<CurrencyTradingInfoResponse> buyCurrencyBySymbol(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol", currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.buyCurrencyBySymbol(currencySymbol, getUserId()));
    }

    @GetMapping("/sellCurrencyBySymbol")
    @ApiOperation("售出该商户币")
    @ApiImplicitParam(paramType="query", name = "currencySymbol", value = "币的类型 -> 状态码MSG_4002 跳转（我要挂卖单）", required = true, dataType = "String")
    public RootResponse<CurrencyTradingInfoResponse> sellCurrencyBySymbol(String currencySymbol) throws Exception {
        this.validateRequired("currencySymbol", currencySymbol);
        return buildSuccess(this.currencyMallServiceImpl.sellCurrencyBySymbol(currencySymbol, getUserId()));
    }

    private Long getUserId() {
        return this.getLoginInfo().getId();
    }
}