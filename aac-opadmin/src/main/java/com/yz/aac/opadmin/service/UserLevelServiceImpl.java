package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.exception.SerializationException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.opadmin.model.request.CreateUserLevelRequest;
import com.yz.aac.opadmin.model.request.QueryUserLevelRequest;
import com.yz.aac.opadmin.model.request.UpdateUserLevelRequest;
import com.yz.aac.opadmin.model.response.QueryUserLevelResponse;
import com.yz.aac.opadmin.repository.UserLevelRepository;
import com.yz.aac.opadmin.repository.domain.UserLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.ErrorMessage.EXISTS_NAME;
import static com.yz.aac.opadmin.Constants.ErrorMessage.TARGET_DATA_MISSING;

@Service
@Slf4j
public class UserLevelServiceImpl implements UserLevelService {

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private FileStorageHandler fileStorageHandler;

    @Override
    public QueryUserLevelResponse queryUserLevels(QueryUserLevelRequest request) {
        UserLevel param = new UserLevel();
        param.setName(request.getName());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<UserLevel> page = (Page<UserLevel>) userLevelRepository.query(param);
        List<QueryUserLevelResponse.Item> items = page.getResult()
                .stream()
                .map(x -> {
                    try {
                        return new QueryUserLevelResponse.Item(
                                x.getId(),
                                x.getName(),
                                fileStorageHandler.genDownloadUrl(x.getIconPath()),
                                x.getMatchCondition()
                        );
                    } catch (SerializationException e) {
                        log.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        return new QueryUserLevelResponse(page.getTotal(), items);
    }

    @Override
    public void createUserLevel(CreateUserLevelRequest request) throws Exception {
        //检查名称是否重复
        UserLevel param = new UserLevel();
        param.setAccurateName(request.getName().trim());
        if (!userLevelRepository.query(param).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //写入
        userLevelRepository.store(new UserLevel(
                null,
                request.getName().trim(),
                null,
                fileStorageHandler.uploadFile(request.getIcon().getContent(), request.getIcon().getExtName()),
                new BigDecimal(request.getMatchCondition())
        ));
    }

    @Override
    public void updateUserLevel(UpdateUserLevelRequest request) throws Exception {
        //检查名称是否重复
        UserLevel param = new UserLevel();
        param.setAccurateName(request.getName().trim());
        List<UserLevel> items = userLevelRepository.query(param);
        if (!items.isEmpty() && items.iterator().next().getId() != request.getId().longValue()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //查询
        param = new UserLevel();
        param.setId(request.getId());
        items = userLevelRepository.query(param);
        if (items.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        UserLevel item = items.iterator().next();
        //更新
        String iconPath = item.getIconPath();
        if (request.getUpdateIcon()) {
            fileStorageHandler.deleteFile(iconPath);
            iconPath = fileStorageHandler.uploadFile(request.getIcon().getContent(), request.getIcon().getExtName());
        }
        userLevelRepository.update(new UserLevel(
                item.getId(),
                request.getName().trim(),
                null,
                iconPath,
                new BigDecimal(request.getMatchCondition())
        ));
    }

}
