package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.opadmin.model.request.QueryFinancialLedgerRequest;
import com.yz.aac.opadmin.model.response.QueryFinancialLedgerResponse;
import com.yz.aac.opadmin.repository.PlatformAssertIncomeExpenditureRecordRepository;
import com.yz.aac.opadmin.repository.UserRoleRepository;
import com.yz.aac.opadmin.repository.domain.PlatformAssertIncomeExpenditureRecord;
import com.yz.aac.opadmin.repository.domain.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.LedgerType;
import static com.yz.aac.opadmin.Constants.UserRole.*;

@Service
@Slf4j
public class FinancialServiceImpl implements FinancialService {

    @Autowired
    private PlatformAssertIncomeExpenditureRecordRepository recordRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public QueryFinancialLedgerResponse queryLedgers(QueryFinancialLedgerRequest request) {
        //查询进出帐记录
        PlatformAssertIncomeExpenditureRecord param = new PlatformAssertIncomeExpenditureRecord();
        param.setBusiness(request.getBusiness());
        param.setDirection(request.getDirection());
        param.setUserRole(request.getUserRole());
        param.setBeginActionTime(request.getBeginTime());
        param.setEndActionTime(request.getEndTime());
        param.setUserName(request.getUserName());
        param.setId(request.getId());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<PlatformAssertIncomeExpenditureRecord> page = (Page<PlatformAssertIncomeExpenditureRecord>) recordRepository.query(param);
        Set<Long> userIds = page.getResult().stream().map(PlatformAssertIncomeExpenditureRecord::getUserId).collect(Collectors.toSet());
        List<QueryFinancialLedgerResponse.Item> items = new ArrayList<>();
        if (!userIds.isEmpty()) {
            Map<Long, UserRole> roleMap = findUserRoleMap(userIds);
            items = page.getResult()
                    .stream()
                    .map(x -> new QueryFinancialLedgerResponse.Item(
                            x.getId(),
                            x.getUserId(),
                            mapUserRoles(x.getUserId(), x.getAction(), roleMap),
                            x.getUserName(),
                            x.getActionTime(),
                            x.getAmount(),
                            x.getRmbAmount(),
                            x.getAction()
                    )).collect(Collectors.toList());
        }
        //查询进出帐金额统计
        PlatformAssertIncomeExpenditureRecord sum = new PlatformAssertIncomeExpenditureRecord();
        sum.setSumAmount(new BigDecimal(0));
        sum.setSumRmbAmount(new BigDecimal(0));
        if (request.getOutputStatistics()) {
            List<PlatformAssertIncomeExpenditureRecord> records = recordRepository.querySumAmount(param);
            if (!records.isEmpty()) {
                PlatformAssertIncomeExpenditureRecord item = records.iterator().next();
                if (null != item) {
                    sum = item;
                }
            }
        }
        return new QueryFinancialLedgerResponse(
                sum.getSumAmount(),
                sum.getSumRmbAmount(),
                page.getTotal(),
                items
        );
    }

    private Map<Long, UserRole> findUserRoleMap(Set<Long> userIds) {
        UserRole param = new UserRole();
        param.setUserIds(userIds);
        Map<Long, UserRole> roleMap = new HashMap<>();
        userRoleRepository.query(param).forEach(x -> roleMap.put(x.getUserId(), x));
        return roleMap;
    }

    @SuppressWarnings("ConstantConditions")
    private Set<Integer> mapUserRoles(Long userId, Integer action, Map<Long, UserRole> roleMap) {
        LedgerType ledgerType = Arrays.stream(LedgerType.values()).filter(x -> x.value() == action).findFirst().orElseGet(null);
        Set<Integer> roles = new HashSet<>();
        switch (ledgerType) {
            case ISSUANCE_DEPOSIT:
                roles.add(MERCHANT.value());
                break;
            case AD_CHARGE:
                roles.add(ADVERTISER.value());
                break;
            case RECHARGE_PLATFORM_CURRENCY:
            case SERVICE_CHARGE:
            case TRANSFER_PLATFORM_CURRENCY:
                roles.add(GENERAL.value());
                UserRole role = roleMap.get(userId);
                if (role.getIsMerchant() == YES.value()) {
                    roles.add(MERCHANT.value());
                }
                if (role.getIsAdvertiser() == YES.value()) {
                    roles.add(ADVERTISER.value());
                }
                break;
        }
        return roles;
    }
}
