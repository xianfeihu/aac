package com.yz.aac.opadmin.service;

import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.QueryConfigRequest;
import com.yz.aac.opadmin.model.request.UpdateConfigRequest;
import com.yz.aac.opadmin.model.response.QueryConfigResponse;
import com.yz.aac.opadmin.repository.ParamConfigRepository;
import com.yz.aac.opadmin.repository.domain.ParamConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.Config.*;
import static com.yz.aac.opadmin.Constants.ErrorMessage.INVALID_VALUE;
import static com.yz.aac.opadmin.Constants.ErrorMessage.UPDATE_READONLY_DATA_DENY;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ParamConfigRepository paramConfigRepository;

    @Override
    public List<QueryConfigResponse> queryConfigs(QueryConfigRequest request) {
        return paramConfigRepository.query(new ParamConfig(
                null,
                request.getCategory(),
                request.getSubCategory(),
                StringUtils.isBlank(request.getKey()) ? null : request.getKey().trim(),
                null
        )).stream().map(x -> {
            String value = x.getValue();
            if (RMB_EXCHANGE_RATE.category() == x.getCategory() && RMB_EXCHANGE_RATE.subCategory() == x.getSubCategory() && RMB_EXCHANGE_RATE.key().equals(x.getKey())) {
                value = PLATFORM_CURRENCY_EXCHANGE_RATE.value();
            }
            return new QueryConfigResponse(x.getCategory(), x.getSubCategory(), x.getKey(), value);
        }).collect(Collectors.toList());
    }

    @Override
    public void updateConfig(UpdateConfigRequest request) throws Exception {
        boolean passed = true;
        if (READING_TIME.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 100);
        } else if (MAX_READING_PER_DAY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 100);
        } else if (READING_CURRENCY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 100);
        } else if (MIN_ARTICLE_LENGTH.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 200, 500);
        } else if (MAX_PUBLISHING_PER_DAY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 100);
        } else if (PUBLISHING_CURRENCY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 100);
        } else if (REAL_NAME_VERIFICATION_CURRENCY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 999);
        } else if (SING_IN_POINT.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 999);
        } else if (ATTENTION_PUBLIC_NUMBER_POINT.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 999);
        } else if (MAX_INVITATION_PER_DAY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 999);
        } else if (INVITATION_CURRENCY.key().equals(request.getKey())) {
            passed = validateDoubleValue(request.getValue(), 1D, 999D);
        } else if (MINING_ROYALTY_RATE.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 999);
        } else if (GRAB_RED_PACKET_PER_DAY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 1000);
        } else if (MAX_PARTICIPATION_PER_DAY.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 10);
        } else if (QUESTIONS_PER_PARTICIPATION.key().equals(request.getKey())) {
            passed = validateIntValue(request.getValue(), 1, 15);
        } else if (PRICE_FLOATING_RATE.key().equals(request.getKey())) {
            passed = validateDoubleValue(request.getValue(), 1D, 100D);
        }
        if (!passed) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_VALUE.value().replaceFirst(PLACEHOLDER.value(), request.getValue()));
        }
        if (RMB_EXCHANGE_RATE.category() == request.getCategory() && RMB_EXCHANGE_RATE.subCategory() == request.getSubCategory() && RMB_EXCHANGE_RATE.key().equals(request.getKey())) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), UPDATE_READONLY_DATA_DENY.value());
        }
        paramConfigRepository.update(new ParamConfig(
                null, request.getCategory(), request.getSubCategory(), request.getKey(), request.getValue()
        ));
    }

    private boolean validateIntValue(String value, int min, int max) {
        boolean passed;
        try {
            int candidate = Integer.parseInt(value);
            passed = (candidate >= min && candidate <= max);
        } catch (Exception e) {
            passed = false;
        }
        return passed;
    }

    private boolean validateDoubleValue(String value, double min, double max) {
        boolean passed;
        try {
            double candidate = Double.valueOf(value);
            passed = (candidate >= min && candidate <= max);
        } catch (Exception e) {
            passed = false;
        }
        return passed;
    }
}
