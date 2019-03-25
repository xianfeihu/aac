package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.CreateMiningActivityRequest;
import com.yz.aac.opadmin.model.request.QueryMiningActivityParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryMiningActivityParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryMiningActivityResponse;
import com.yz.aac.opadmin.repository.MiningActivityItemRepository;
import com.yz.aac.opadmin.repository.MiningActivityRepository;
import com.yz.aac.opadmin.repository.MiningActivityStatisticsRepository;
import com.yz.aac.opadmin.repository.UserLevelRepository;
import com.yz.aac.opadmin.repository.domain.MiningActivity;
import com.yz.aac.opadmin.repository.domain.MiningActivityItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.ErrorMessage.INVALID_USER_CODE;
import static com.yz.aac.opadmin.Constants.MiningActivityStatus.CANCELED;
import static com.yz.aac.opadmin.Constants.MiningActivityStatus.GENERAL;

@Service
@Slf4j
public class MiningServiceImpl implements MiningService {

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private MiningActivityRepository miningActivityRepository;

    @Autowired
    private MiningActivityItemRepository miningActivityItemRepository;

    @Autowired
    private MiningActivityStatisticsRepository miningActivityStatisticsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createActivity(CreateMiningActivityRequest request) throws Exception {
        long sysTime = System.currentTimeMillis();
        int maxActivities = 10;
        long delay = 1000 * 60 * 5;
        long minDuration = 1000 * 60;
        long maxDuration = 1000 * 60 * 10;
        long lastEndTime = -1;
        Map<Long, String> allUserLevels = new HashMap<>();
        allUserLevels.put(null, "不限等级");
        if (request.getActivities().size() > maxActivities) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("最多只能添加%d场活动", maxActivities));
        }
        userLevelRepository.query(null).forEach(x -> allUserLevels.put(x.getId(), x.getName()));
        List<MiningActivity> activities = miningActivityRepository.query(null);
        for (MiningActivity x : activities) {
            //若目前尚有未结束的活动场次，则不可创建新的
            if (x.getStatus() == GENERAL.value() && x.getEndTime() > sysTime) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "无效请求！目前尚有未结束的活动场次");
            }
            //若目前已有尚未到达开始时间的活动场次，则不可创建新的
            else if (x.getBeginTime() > sysTime && x.getStatus() == GENERAL.value()) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "无效请求！目前已有尚未到达开始时间的活动场次");
            }
        }
        for (int i = 0; i < request.getActivities().size(); i++) {
            //每场活动开始时间必须设定在当前时间5分钟之后
            CreateMiningActivityRequest.Activity activity = request.getActivities().get(i);
            if (activity.getBeginTime() < (sysTime + delay)) {
                //TODO 临时去掉，便于调试
//                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("第%d场活动开始时间必须设定在当前时间%d分钟之后", i + 1, delay / 1000 / 60));
            }
            //每场活动持续时间必须在1-10分钟之间
            long timeOffset = activity.getEndTime() - activity.getBeginTime();
            if (timeOffset < minDuration || timeOffset > maxDuration) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("第%d场活动持续时间必须在%d-%d分钟之间", i + 1, minDuration / 1000 / 60, maxDuration / 1000 / 60));
            }
            //每场活动起止时间不能存在冲突
            if (activity.getBeginTime() < lastEndTime) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("第%d场活动持续时间与上一场存在冲突", i + 1));
            }
            lastEndTime = activity.getEndTime();
            List<Long> sessionUserIds = activity.getSessions().stream().map(CreateMiningActivityRequest.Activity.Session::getUserLevelId).collect(Collectors.toList());
            for (int j = 0; j < activity.getSessions().size(); j++) {
                //验证用户等级ID有效性
                CreateMiningActivityRequest.Activity.Session session = activity.getSessions().get(j);
                if (!allUserLevels.keySet().contains(session.getUserLevelId())) {
                    throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("第%d场活动第%d个入口，无效的用户等级ID：[%d]", i + 1, j + 1, session.getUserLevelId()));
                }
                //每场活动同一用户等级限制入口只能定义一次（包括不限制）
                long count = sessionUserIds.stream().filter(x -> {
                    if (null == session.getUserLevelId()) {
                        return x == null;
                    } else {
                        return (null != x && x.longValue() == session.getUserLevelId());
                    }
                }).count();
                if (count > 1) {
                    throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("第%d场活动，进入等级限制：[%s]，只能定义一次", i + 1, allUserLevels.get(session.getUserLevelId())));
                }
            }
            //若定义了不限等级入口，则不可再定义其他活动入口
            if (activity.getSessions().size() > 1 && sessionUserIds.stream().anyMatch(Objects::isNull)) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("第%d场活动，已定义了不限等级入口，则不可再定义其他任意等级入口", i + 1));
            }
        }
        //删除所有已取消的活动场次
        Set<Long> canceledActIds = activities.stream().filter(x -> x.getStatus() == CANCELED.value()).map(MiningActivity::getId).collect(Collectors.toSet());
        if (!canceledActIds.isEmpty()) {
            MiningActivity activity = new MiningActivity();
            activity.setIds(canceledActIds);
            miningActivityRepository.delete(activity);
            MiningActivityItem item = new MiningActivityItem();
            item.setActivityIds(canceledActIds);
            miningActivityItemRepository.delete(item);
        }
        for (CreateMiningActivityRequest.Activity act : request.getActivities()) {
            //保存场次
            MiningActivity activity = new MiningActivity(null, null, act.getBeginTime(), act.getEndTime(), GENERAL.value());
            miningActivityRepository.store(activity);
            for (int i = 0; i < act.getSessions().size(); i++) {
                //保存入口
                CreateMiningActivityRequest.Activity.Session session = act.getSessions().get(i);
                MiningActivityItem item = new MiningActivityItem(
                        null,
                        activity.getId(),
                        null,
                        session.getUserLevelId(),
                        session.getTotalBonus(),
                        session.getLuckyRate(),
                        session.getLuckyTimes(),
                        session.getHitAdNumber(),
                        i + 1
                );
                miningActivityItemRepository.store(item);
            }
        }
    }

    @Override
    public void cancelActivity() throws Exception {
        long delay = 1000 * 60 * 5;
        long sysTime = System.currentTimeMillis();
        List<MiningActivity> cancelableActivities = miningActivityRepository.query(new MiningActivity(null, null, sysTime, null, GENERAL.value()));
        if (cancelableActivities.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "没有找到可取消的活动");
        }
        for (int i = 0; i < cancelableActivities.size(); i++) {
            MiningActivity activity = cancelableActivities.get(i);
            if (activity.getBeginTime() < (sysTime + delay)) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), String.format("此时距离第%d场活动开始已不足%d分钟，无法结束", i + 1, delay / 1000 / 60));
            }
            //更新状态
            activity.setStatus(CANCELED.value());
            miningActivityRepository.update(activity);
        }
    }

    @Override
    public QueryMiningActivityResponse queryActivities() {
        long sysTime = System.currentTimeMillis();
        List<CreateMiningActivityRequest.Activity> activityData = new ArrayList<>(0);
        List<MiningActivity> activities = miningActivityRepository.query(new MiningActivity(null, null, sysTime, null, null));
        int status;
        //若未查询到活动或查询到的活动已取消，则可以[开始]
        if (activities.isEmpty() || activities.iterator().next().getStatus() == CANCELED.value()) {
            status = CANCELED.value();
        } else {
            //查询到活动且尚未取消，则可以[取消]
            status = GENERAL.value();
        }
        if (!activities.isEmpty()) {
            Set<Long> actIds = activities.stream().map(MiningActivity::getId).collect(Collectors.toSet());
            MiningActivityItem item = new MiningActivityItem();
            item.setActivityIds(actIds);
            List<MiningActivityItem> items = miningActivityItemRepository.query(item);
            activityData = activities.stream().map(a -> {
                List<CreateMiningActivityRequest.Activity.Session> sessions = items
                        .stream()
                        .filter(s -> s.getActivityId() == a.getId().longValue())
                        .map(s -> new CreateMiningActivityRequest.Activity.Session(
                                s.getUserLevelId(),
                                s.getTotalBonus(),
                                s.getLuckyRate(),
                                s.getLuckyTimes(),
                                s.getHitAdNumber()
                        )).collect(Collectors.toList());
                return new CreateMiningActivityRequest.Activity(a.getBeginTime(), a.getEndTime(), sessions);
            }).collect(Collectors.toList());
        }
        return new QueryMiningActivityResponse(activityData, status);
    }

    @Override
    public QueryMiningActivityParticipatorResponse queryMiningActivityParticipators(QueryMiningActivityParticipatorRequest request) throws Exception {
        Long userId;
        try {
            userId = StringUtils.isBlank(request.getCode()) ? null : Long.parseLong(request.getCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        request.setId(userId);
        request.setName(StringUtils.isBlank(request.getName()) ? null : request.getName().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryMiningActivityParticipatorResponse.Item> page = (Page<QueryMiningActivityParticipatorResponse.Item>) miningActivityStatisticsRepository.query(request);
        List<QueryMiningActivityParticipatorResponse.Item> items = page.getResult().stream()
                .map(x -> new QueryMiningActivityParticipatorResponse.Item(
                        USER_CODE_PREFIX.value() + x.getUserCode(),
                        x.getUserName(),
                        x.getTotalCount(),
                        x.getAdClickedCount(),
                        x.getTotalBonus()
                )).collect(Collectors.toList());
        return new QueryMiningActivityParticipatorResponse(page.getTotal(), items);
    }
}
