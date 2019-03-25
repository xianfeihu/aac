package com.yz.aac.common.exception;

public class RpcException extends Exception {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Exception e) {
        super(e);
    }

    @Override
    public String toString() {
        return String.format("RpcException(\"%s\")", getMessage());
    }

}

