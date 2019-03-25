package com.yz.aac.common.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_SUCCESS;
import static com.yz.aac.common.Constants.ResponseStatusInfo.HTTP_OK;

@SuppressWarnings("unchecked")
@Data
@AllArgsConstructor
@ApiModel("全局响应消息体")
public class RootResponse<T> {

    @ApiModelProperty(value = "HTTP状态码", position = 1, required = true)
    private int statusCode;

    @ApiModelProperty(value = "消息码", position = 2, required = true)
    private String messageCode;

    @ApiModelProperty(value = "消息描述", position = 3, required = true)
    private String message;

    @ApiModelProperty(position = 4)
    private T content;

    public static <T> RootResponse<T> buildSuccess(T content) {
        return new RootResponse(HTTP_OK.code(), MSG_SUCCESS.code(), MSG_SUCCESS.message(), content);
    }

    public static <T> RootResponse<T> buildError(int statusCode, String messageCode, String message) {
        return new RootResponse(statusCode, messageCode, message, null);
    }
}