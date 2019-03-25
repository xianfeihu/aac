package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.opadmin.model.request.QueryRedPacketRequest;
import com.yz.aac.opadmin.model.response.QueryRedPacketDetailResponse;
import com.yz.aac.opadmin.model.response.QueryRedPacketResponse;
import com.yz.aac.opadmin.repository.RedPacketIssuanceRepository;
import com.yz.aac.opadmin.repository.domain.RedPacketIssuance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private FileStorageHandler fileStorageHandler;

    @Autowired
    private RedPacketIssuanceRepository redPacketIssuanceRepository;

    @Override
    public QueryRedPacketResponse queryRedPackets(QueryRedPacketRequest request) throws Exception {
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryRedPacketResponse.Item> page = (Page<QueryRedPacketResponse.Item>) redPacketIssuanceRepository.query(new RedPacketIssuance(
                StringUtils.isBlank(request.getName()) ? null : request.getName().trim()
        ));
        List<QueryRedPacketResponse.Item> items = page.getResult().stream()
                .map(x -> new QueryRedPacketResponse.Item(
                        x.getId(),
                        x.getName(),
                        x.getIssuanceTime(),
                        x.getLocation(),
                        x.getRadius(),
                        x.getDividingNumber(),
                        x.getCurrencyAmount(),
                        x.getGrabberCount()
                )).collect(Collectors.toList());
        return new QueryRedPacketResponse(page.getTotal(), items);
    }

    @Override
    public QueryRedPacketDetailResponse queryRedPacketDetail(Long id) throws Exception {
        QueryRedPacketDetailResponse result = redPacketIssuanceRepository.queryDetail(id);
        result.setPrimaryImageUrl(fileStorageHandler.genDownloadUrl(result.getPrimaryImageUrl()));
        return result;
    }
}
