package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.CreateMiningActivityRequest;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryMiningActivityParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryMiningActivityParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryMiningActivityResponse;
import com.yz.aac.opadmin.service.MiningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/mining")
@Slf4j
@Api(tags = "挖矿活动（app内容管理-挖）")
public class MiningController extends BaseController {

    @Autowired
    private MiningService miningService;

    @ApiOperation("开始活动")
    @PostMapping("/activities")
    @ResponseBody
    public RootResponse<?> createActivity(@RequestBody CreateMiningActivityRequest request) throws Exception {
        if (CollectionUtils.isEmpty(request.getActivities())) {
            validateRequired(ACTIVITIES.value(), null);
        }
        for (int i = 0; i < request.getActivities().size(); i++) {
            CreateMiningActivityRequest.Activity activity = request.getActivities().get(i);
            String activityFieldPrefix = String.format("%s.%d.", ACTIVITIES.value(), i);
            validateRequired(activityFieldPrefix + BEGIN_TIME.value(), activity.getBeginTime());
            validateRequired(activityFieldPrefix + END_TIME.value(), activity.getEndTime());
            validateTimePeriod(activityFieldPrefix + BEGIN_TIME.value(), activity.getBeginTime(), activityFieldPrefix + END_TIME.value(), activity.getEndTime());
            if (CollectionUtils.isEmpty(activity.getSessions())) {
                validateRequired(activityFieldPrefix + SESSIONS.value(), null);
            }
            for (int j = 0; j < activity.getSessions().size(); j++) {
                CreateMiningActivityRequest.Activity.Session session = activity.getSessions().get(j);
                String sessionFieldPrefix = String.format("%s%s.%d.", activityFieldPrefix, SESSIONS.value(), j);
                validateRequired(sessionFieldPrefix + TOTAL_BONUS.value(), session.getTotalBonus());
                validateBigDecimalRange(sessionFieldPrefix + TOTAL_BONUS.value(), session.getTotalBonus(), BigDecimal.valueOf(1), BigDecimal.valueOf(10000));
                validateRequired(sessionFieldPrefix + LUCKY_RATE.value(), session.getLuckyRate());
                validateIntRange(sessionFieldPrefix + LUCKY_RATE.value(), session.getLuckyRate(), 1, 100);
                validateRequired(sessionFieldPrefix + LUCK_TIMES.value(), session.getLuckyTimes());
                validateIntRange(sessionFieldPrefix + LUCK_TIMES.value(), session.getLuckyTimes(), 1, 10);
                validateRequired(sessionFieldPrefix + HIT_AD_NUMBER.value(), session.getHitAdNumber());
                validateIntRange(sessionFieldPrefix + HIT_AD_NUMBER.value(), session.getHitAdNumber(), 5, 50);
            }
        }
        miningService.createActivity(request);
        return buildSuccess(null);
    }

    @ApiOperation("取消活动")
    @PostMapping("/activities/cancel")
    @ResponseBody
    public RootResponse<?> cancelActivity() throws Exception {
        miningService.cancelActivity();
        return buildSuccess(null);
    }

    @ApiOperation("查询活动")
    @GetMapping("/activities")
    @ResponseBody
    public RootResponse<QueryMiningActivityResponse> queryActivities() throws Exception {
        return buildSuccess(miningService.queryActivities());
    }

    @ApiOperation("查询参与用户")
    @GetMapping("/activities/participators")
    @ResponseBody
    public RootResponse<QueryMiningActivityParticipatorResponse> queryParticipators(
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        if (StringUtils.isNotBlank(name)) {
            validateSpecialChar(NAME.value(), name);
            validateStringLength(NAME.value(), name, 1, 32);
        }
        if (StringUtils.isNotBlank(code)) {
            validateSpecialChar(CODE.value(), code);
            validateStringLength(CODE.value(), code, 1, 16);
        }
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        return buildSuccess(miningService.queryMiningActivityParticipators(
                new QueryMiningActivityParticipatorRequest(name, code, null, new PagingRequest(pageNumber, pageSize))
        ));
    }

}