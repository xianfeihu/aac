package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.LoginResponse;
import com.yz.aac.opadmin.model.response.QueryAccessLogResponse;
import com.yz.aac.opadmin.model.response.QueryAccountResponse;
import com.yz.aac.opadmin.repository.OperatorActionLogRepository;
import com.yz.aac.opadmin.repository.OperatorRepository;
import com.yz.aac.opadmin.repository.domain.Operator;
import com.yz.aac.opadmin.repository.domain.OperatorActionLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.*;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorActionLogRepository operatorActionLogRepository;

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        Exception ex = null;
        List<Operator> operators = operatorRepository.query(
                new Operator(null, request.getLoginName().trim(), null, null, null, null, null, null, null)
        );
        Operator operator = operators.isEmpty() ? null : operators.iterator().next();
        if (null == operator || !operator.getPassword().equals(request.getPassword().trim())) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_LOGIN_NAME_OR_PASSWORD.value());
        } else if (operator.getStatus() == AccountStatus.DISABLED.value()) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DISABLED_ACCOUNT.value());
        }
        if (null != ex) {
            throw ex;
        }
        return new LoginResponse(
                operator.getId(),
                Arrays.stream(new Integer[]{operator.getRole()}).collect(Collectors.toSet()),
                operator.getName()
        );
    }

    @Override
    public void createAccount(CreateAccountRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        List<Operator> operators = operatorRepository.query(
                new Operator(null, request.getLoginName().trim(), null, null, null, null, null, null, null)
        );
        if (!operators.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_ACCOUNT.value());
        }
        operatorRepository.store(new Operator(
                        null,
                        request.getLoginName(),
                        request.getPassword(),
                        request.getName(),
                        request.getDepartment(),
                        AccountStatus.ENABLED.value(),
                        OperatorRole.OPERATOR.value(),
                        sysDate,
                        sysDate
                )
        );
    }

    @Override
    public void disableAccount(DisableAccountRequest request, Long loginId) throws Exception {
        Exception ex = null;
        Operator param = new Operator();
        param.setId(loginId);
        List<Operator> operators = operatorRepository.query(param);
        Operator loginOperator = operators.isEmpty() ? null : operators.iterator().next();
        if (null == loginOperator || loginOperator.getRole() != OperatorRole.SUPER_ADMIN.value()) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ACCESS_DENY.value());
        }
        param.setId(request.getId());
        operators = operatorRepository.query(param);
        Operator targetOperator = operators.isEmpty() ? null : operators.iterator().next();
        if (null == targetOperator) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_ID.value());
        } else if (targetOperator.getRole() == OperatorRole.SUPER_ADMIN.value()) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ACCESS_DENY.value());
        }
        if (null != ex) {
            throw ex;
        }
        targetOperator.setStatus(AccountStatus.DISABLED.value());
        targetOperator.setUpdateTime(System.currentTimeMillis());
        operatorRepository.update(targetOperator);
    }

    @Override
    public void enableAccount(EnableAccountRequest request, Long loginId) throws Exception {
        Exception ex = null;
        Operator param = new Operator();
        param.setId(loginId);
        List<Operator> operators = operatorRepository.query(param);
        Operator loginOperator = operators.isEmpty() ? null : operators.iterator().next();
        if (null == loginOperator || loginOperator.getRole() != OperatorRole.SUPER_ADMIN.value()) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ACCESS_DENY.value());
        }
        param.setId(request.getId());
        operators = operatorRepository.query(param);
        Operator targetOperator = operators.isEmpty() ? null : operators.iterator().next();
        if (null == targetOperator) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_ID.value());
        } else if (targetOperator.getRole() == OperatorRole.SUPER_ADMIN.value()) {
            ex = new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ACCESS_DENY.value());
        }
        if (null != ex) {
            throw ex;
        }
        targetOperator.setStatus(AccountStatus.ENABLED.value());
        targetOperator.setUpdateTime(System.currentTimeMillis());
        operatorRepository.update(targetOperator);
    }

    @Override
    public QueryAccountResponse queryAccounts(QueryAccountRequest request) {
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<Operator> page = (Page<Operator>) operatorRepository.query(
                new Operator(null, request.getLoginName(), null, request.getName(), null, null, null, null, null)
        );
        List<QueryAccountResponse.Item> items = page.getResult()
                .stream()
                .map(x -> new QueryAccountResponse.Item(x.getId(), x.getLoginName(), x.getName(), x.getDepartment(), x.getRole(), x.getStatus()))
                .collect(Collectors.toList());
        return new QueryAccountResponse(page.getTotal(), items);
    }

    @Override
    public QueryAccessLogResponse queryAccessLogs(QueryAccessLogRequest request, Long loginId) throws Exception {
        Operator param = new Operator();
        param.setId(loginId);
        List<Operator> operators = operatorRepository.query(param);
        Operator loginOperator = operators.isEmpty() ? null : operators.iterator().next();
        if (null == loginOperator || loginOperator.getRole() != OperatorRole.SUPER_ADMIN.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ACCESS_DENY.value());
        }
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());

        Page<OperatorActionLog> page = (Page<OperatorActionLog>) operatorActionLogRepository.query(
                new OperatorActionLog(null, request.getOperatorId(), null, null, null, null, request.getBeginTime(), request.getEndTime())
        );
        List<QueryAccessLogResponse.Item> items = page.getResult()
                .stream()
                .map(x -> new QueryAccessLogResponse.Item(
                        x.getActionTime(),
                        findModuleDesc(x.getModule()),
                        findModuleActionDesc(x.getModule(), x.getAction()),
                        x.getAdditionalInfo())
                )
                .collect(Collectors.toList());
        return new QueryAccessLogResponse(page.getTotal(), items);
    }

    private String findModuleDesc(int id) {
        String result = null;
        Optional<FeatureModule> module = Arrays.stream(FeatureModule.values()).filter(x -> x.id() == id).findFirst();
        if (module.isPresent()) {
            result = module.get().description();
        }
        return result;
    }

    private String findModuleActionDesc(int moduleId, int actionId) {
        String result = null;
        Optional<FeatureModuleAction> module = Arrays.stream(FeatureModuleAction.values()).filter(x -> x.moduleId() == moduleId && x.actionId() == actionId).findFirst();
        if (module.isPresent()) {
            result = module.get().description();
        }
        return result;
    }

}
