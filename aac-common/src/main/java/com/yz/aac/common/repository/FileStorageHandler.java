package com.yz.aac.common.repository;


import com.yz.aac.common.config.FileStorageConfig;
import com.yz.aac.common.exception.RpcException;
import com.yz.aac.common.exception.SerializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Properties;

import static org.csource.fastdfs.ClientGlobal.PROP_KEY_HTTP_SECRET_KEY;

@SuppressWarnings("unused")
@ConditionalOnProperty(name = "fileStorage.enabled", havingValue = "true")
@Repository
@Slf4j
public class FileStorageHandler {

    @Autowired
    private FileStorageConfig config;

    private StorageClient1 client = null;

    @PostConstruct
    private void init() throws RpcException {
        Properties props = new Properties();
        props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, String.format("%s:%d", config.getHost(), config.getPort()));
        props.put(PROP_KEY_HTTP_SECRET_KEY, config.getSecurityKey());
        try {
            ClientGlobal.initByProperties(props);
            TrackerServer trackerServer = new TrackerClient().getConnection();
            client = new StorageClient1(trackerServer, null);
            ProtoCommon.activeTest(trackerServer.getSocket());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RpcException(e);
        }
    }

    public String uploadFile(byte[] content, String extName) throws RpcException {

        try {
            String fileKey = client.upload_file1(content, extName, null);
            log.info("Uploaded file: [{}]", fileKey);
            return fileKey;
        } catch (Exception e) {
            throw new RpcException(e);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public void deleteFile(String fileKey) throws RpcException {
        if (StringUtils.isNotBlank(fileKey)) {
            try {
                client.delete_file1(fileKey);
                log.info("Deleted file: [{}]", fileKey);
            } catch (Exception e) {
                throw new RpcException(e);
            }
        }
    }

    public String genDownloadUrl(String fileKey) throws SerializationException {
        int ts = (int) Instant.now().getEpochSecond();
        return String.format("http://%s:%d/%s?token=%s&ts=%d", config.getHost(), config.getHttpPort(), fileKey, genToken(fileKey, ts), ts);
    }

    private String genToken(String fileKey, int ts) throws SerializationException {
        String fileId = fileKey.substring(fileKey.indexOf("/") + 1);
        try {
            return ProtoCommon.getToken(fileId, ts, config.getSecurityKey());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SerializationException(e);
        }
    }

}
