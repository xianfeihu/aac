package com.yz.aac.mining.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.mining.model.request.MiningForSettlementAmountRequest;
import com.yz.aac.mining.model.response.ExcavationGameIndexInfoResponse;
import com.yz.aac.mining.model.response.JoinExcavationGameResponse;
import com.yz.aac.mining.service.ExcavationGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@RestController
@RequestMapping(value = "/game")
@Slf4j
@Api(tags = "挖矿-游戏")
public class ExcavationGameController extends BaseController {

    @Autowired
    private ExcavationGameService excavationGameServiceImpl;
    
    @ApiOperation(value = "挖矿游戏-首页信息")
    @GetMapping("/excavationGameIndexInfo")
    public RootResponse<List<ExcavationGameIndexInfoResponse>> getExcavationGameIndexInfo() throws Exception {

        return buildSuccess(this.excavationGameServiceImpl.getExcavationGameIndexInfo(getUserId()));
    }

    @ApiOperation(value = "挖矿游戏-点击进入(游戏等待页)")
    @GetMapping("/joinExcavationGame")
    @ApiImplicitParam(paramType = "query", name = "activityId", value = "挖矿活动ID", required = true, dataType = "Long")
    public RootResponse<JoinExcavationGameResponse> joinExcavationGame(Long activityId) throws Exception {
        this.validateRequired("挖矿活动ID", activityId);
        return buildSuccess(this.excavationGameServiceImpl.joinExcavationGame(getUserId(),activityId));
    }

    @ApiOperation(value = "挖矿游戏-当前挖矿游戏项参与人数(仅返回人数)")
    @GetMapping("/joinExcavationGameNumber")
    @ApiImplicitParam(paramType = "query", name = "itemId", value = "挖矿活动项ID", required = true, dataType = "Long")
    public RootResponse<Integer> joinExcavationGameNumber(Long itemId) throws Exception {
        this.validateRequired("挖矿活动项ID", itemId);
        return buildSuccess(this.excavationGameServiceImpl.joinExcavationGameNumber(itemId));
    }

    @ApiOperation(value = "挖矿游戏-获取本次挖矿最多获取挖矿量（返回MAX挖矿量）")
    @GetMapping("/mostAmountOfMining")
    @ApiImplicitParam(paramType = "query", name = "itemId", value = "挖矿活动项ID", required = true, dataType = "Long")
    public RootResponse<BigDecimal> mostAmountOfMining(Long itemId) throws Exception {
        this.validateRequired("挖矿活动项ID", itemId);
        return buildSuccess(this.excavationGameServiceImpl.mostAmountOfMining(itemId, getUserId()));
    }

    @ApiOperation(value = "挖矿游戏-结算当前游戏数据/游戏结束后5分钟内可成功结算(返回总获得金额)")
    @GetMapping("/settlementAmountForMining")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "itemId", value = "挖矿活动项ID", required = true, dataType = "Long"),
        @ApiImplicitParam(paramType = "query", name = "hitAdNumber", value = "广告点击次数", required = true, dataType = "Integer"),
        @ApiImplicitParam(paramType = "query", name = "ordinaryAmount", value = "本次挖矿正常获取金额", required = true, dataType = "BigDecimal"),
        @ApiImplicitParam(paramType = "query", name = "extraAmount", value = "本次挖矿暴击获取金额", required = true, dataType = "BigDecimal")
    })
    public RootResponse<BigDecimal> settlementAmountForMining(MiningForSettlementAmountRequest request) throws Exception {
        this.validateRequired("挖矿活动项ID", request.getItemId());
        this.validateRequired("广告点击次数", request.getHitAdNumber());
        this.validateRequired("本次挖矿正常获取金额", request.getOrdinaryAmount());
        this.validateRequired("本次挖矿暴击获取金额", request.getExtraAmount());
        request.setUserId(getUserId());
        return buildSuccess(this.excavationGameServiceImpl.settlementAmountForMining(request));
    }

    private Long getUserId() {
//        return 43l;
        return this.getLoginInfo().getId();
    }

}