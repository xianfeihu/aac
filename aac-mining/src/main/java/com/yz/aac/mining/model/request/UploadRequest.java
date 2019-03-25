package com.yz.aac.mining.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.yz.aac.common.Constants.Misc.DOT;

@Data
@AllArgsConstructor
public class UploadRequest {

    private String extName;

    private byte[] content;

    public static UploadRequest createOrNull(MultipartFile file) throws Exception {
        UploadRequest result = null;
        if (null != file && !file.isEmpty()) {
            int idx = file.getOriginalFilename().lastIndexOf(DOT.value());
            String extName = idx < 0 ? "" : file.getOriginalFilename().substring(idx + 1);
            result = new UploadRequest(extName, file.getBytes());
        }
        return result;
    }

}