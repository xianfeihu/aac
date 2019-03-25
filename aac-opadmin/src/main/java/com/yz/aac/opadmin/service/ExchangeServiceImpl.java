package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.model.request.CreateExchangeRequest;
import com.yz.aac.opadmin.model.request.QueryExchangeRecordRequest;
import com.yz.aac.opadmin.model.request.QueryExchangeRequest;
import com.yz.aac.opadmin.model.response.QueryExchangeRecordResponse;
import com.yz.aac.opadmin.repository.ExchangeItemRepository;
import com.yz.aac.opadmin.repository.ExchangeRecordRepository;
import com.yz.aac.opadmin.repository.ExchangeRepository;
import com.yz.aac.opadmin.repository.PlatformAssertIncomeExpenditureRecordRepository;
import com.yz.aac.opadmin.repository.domain.Exchange;
import com.yz.aac.opadmin.repository.domain.ExchangeItem;
import com.yz.aac.opadmin.repository.domain.PlatformAssertIncomeExpenditureRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;
import static com.yz.aac.opadmin.Constants.ExchangeChargingStatus.FINISHED;
import static com.yz.aac.opadmin.Constants.ExchangeChargingStatus.WAITING;
import static com.yz.aac.opadmin.Constants.LedgerInOut.OUT;

@Service
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeItemRepository exchangeItemRepository;

    @Autowired
    private ExchangeRecordRepository exchangeRecordRepository;

    @Autowired
    private PlatformAssertIncomeExpenditureRecordRepository ledgerRepository;

    @Override
    public List<Exchange> queryDeactivated() {
        return exchangeRepository.query(null).stream()
                .filter(x -> x.getActivated() == NO.value())
                .collect(Collectors.toList());
    }

    @Override
    public List<Exchange> queryExchanges(QueryExchangeRequest request) {
        //query exchanges
        Exchange exchange = new Exchange();
        exchange.setId(request.getId());
        List<Exchange> result = exchangeRepository.query(exchange);
        //query items
        if (request.getIncludeItem()) {
            ExchangeItem item = new ExchangeItem();
            item.setExchangeIds(result.stream().map(Exchange::getId).collect(Collectors.toSet()));
            List<ExchangeItem> items = exchangeItemRepository.query(item);
            result.forEach(e -> e.setItems(items.stream().filter(i -> i.getExchangeId() == e.getId().longValue()).collect(Collectors.toList())));
        }
        return result;
    }

    private void validateItemsSize(CreateExchangeRequest request) throws Exception {
        int ITEMS = 10;
        if (CollectionUtils.isEmpty(request.getItems()) || request.getItems().size() > ITEMS) {
            throw new BusinessException(
                    MSG_CUSTOMIZED_EXCEPTION.code(),
                    EXCHANGE_ITEM_LIMIT.value().replaceFirst(PLACEHOLDER.value(), String.valueOf(1)).replaceFirst(PLACEHOLDER.value(), String.valueOf(ITEMS))
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createExchange(CreateExchangeRequest request) throws Exception {
        validateItemsSize(request);
        Exchange exchange = new Exchange();
        exchange.setId(request.getId());
        List<Exchange> exchanges = exchangeRepository.query(exchange);
        if (exchanges.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_ID.value());
        }
        exchange = exchanges.iterator().next();
        if (exchange.getActivated() == YES.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_SERVICE.value());
        }
        exchangeRepository.update(new Exchange(
                request.getId(),
                null,
                null,
                null,
                request.getCustomized(),
                request.getLimitInMonth(),
                YES.value(),
                null,
                null
        ));
        for (int i = 0; i < request.getItems().size(); i++) {
            CreateExchangeRequest.Item item = request.getItems().get(i);
            exchangeItemRepository.store(new ExchangeItem(
                    null,
                    request.getId(),
                    null,
                    item.getRmbAmount(),
                    BigDecimal.valueOf(item.getPlatformAmount()),
                    i + 1
            ));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExchange(CreateExchangeRequest request) throws Exception {
        validateItemsSize(request);
        Exchange exchange = new Exchange();
        exchange.setId(request.getId());
        List<Exchange> exchanges = exchangeRepository.query(exchange);
        if (exchanges.isEmpty() || exchanges.iterator().next().getActivated() == NO.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        exchangeRepository.update(new Exchange(
                request.getId(),
                null,
                null,
                null,
                request.getCustomized(),
                request.getLimitInMonth(),
                YES.value(),
                null,
                null
        ));
        ExchangeItem condition = new ExchangeItem();
        condition.setExchangeId(request.getId());
        exchangeItemRepository.delete(condition);
        for (int i = 0; i < request.getItems().size(); i++) {
            CreateExchangeRequest.Item item = request.getItems().get(i);
            exchangeItemRepository.store(new ExchangeItem(
                    null,
                    request.getId(),
                    null,
                    item.getRmbAmount(),
                    BigDecimal.valueOf(item.getPlatformAmount()),
                    i + 1
            ));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExchange(Long id) throws Exception {
        Exchange exchange = new Exchange();
        exchange.setId(id);
        List<Exchange> exchanges = exchangeRepository.query(exchange);
        if (exchanges.isEmpty() || exchanges.iterator().next().getActivated() == NO.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        exchangeRepository.update(new Exchange(
                id, null, null, null, 0, 0, NO.value(), null, null
        ));
        ExchangeItem item = new ExchangeItem();
        item.setExchangeId(id);
        exchangeItemRepository.delete(item);
    }

    @Override
    public QueryExchangeRecordResponse queryRecords(QueryExchangeRecordRequest request) {
        request.setUserName(StringUtils.isBlank(request.getUserName()) ? null : request.getUserName().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryExchangeRecordResponse.Item> page = (Page<QueryExchangeRecordResponse.Item>) exchangeRecordRepository.query(request);
        Set<Long> userIds = page.stream().map(QueryExchangeRecordResponse.Item::getUserId).collect(Collectors.toSet());
        Set<Long> exchangeIds = page.stream().map(QueryExchangeRecordResponse.Item::getExchangeId).collect(Collectors.toSet());
        request.setExchangeIds(exchangeIds);
        request.setUserIds(userIds);
        request.setBeginTime(parseBeginTimeOfMonth());
        if (!CollectionUtils.isEmpty(userIds)) {
            Map<String, QueryExchangeRecordResponse.Item> userCountMap = new HashMap<>();
            exchangeRecordRepository.count(request).forEach(x -> {
                String key = String.format("%d-%d", x.getUserId(), x.getExchangeId());
                userCountMap.put(key, x);
            });
            page.forEach(x -> {
                String key = String.format("%d-%d", x.getUserId(), x.getExchangeId());
                QueryExchangeRecordResponse.Item value = userCountMap.get(key);
                x.setExchangedTimesInMonth(value.getExchangedTimesInMonth());
                x.setRestTimesInMonth(value.getRestTimesInMonth() < 0 ? 0 : value.getRestTimesInMonth());
            });
        }
        return new QueryExchangeRecordResponse(page.getTotal(), page);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void charging(Long id) throws Exception {
        QueryExchangeRecordRequest condition = new QueryExchangeRecordRequest();
        condition.setId(id);
        List<QueryExchangeRecordResponse.Item> items = exchangeRecordRepository.query(condition);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        QueryExchangeRecordResponse.Item item = items.iterator().next();
        Exchange exchange = new Exchange();
        exchange.setId(item.getExchangeId());
        List<Exchange> exchanges = exchangeRepository.query(exchange);
        if (exchanges.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        if (item.getStatus() != WAITING.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_STATUS.value());
        }
        //更新状态
        item.setStatus(FINISHED.value());
        exchangeRecordRepository.update(item);
        //写入法币出帐
        exchange = exchanges.iterator().next();


        Exchange finalExchange = exchange;
        String typeName = Arrays.stream(Constants.ExchangeChargingType.values()).filter(x -> x.category() == finalExchange.getCategory() && x.subCategory() == finalExchange.getSubCategory()).findFirst().get().name();
        int action = Arrays.stream(Constants.LedgerType.values()).filter(x -> x.name().equals(typeName)).findFirst().get().value();
        ledgerRepository.store(new PlatformAssertIncomeExpenditureRecord(
                null,
                OUT.value(),
                null,
                item.getUserId(),
                item.getUserName(),
                System.currentTimeMillis(),
                null,
                null,
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(item.getRmbAmount()),
                action,
                null,
                null,
                null
        ));
    }

    private static long parseBeginTimeOfMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

}
