package com.yz.aac.common.exception;

import lombok.Getter;

public class BusinessException extends Exception {

    @Getter
    private String messageCode;

    public BusinessException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return String.format("BusinessException(\"%s\", \"%s\")", messageCode, getMessage());
    }
}

