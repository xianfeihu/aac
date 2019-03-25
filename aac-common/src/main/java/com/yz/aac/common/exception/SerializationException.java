package com.yz.aac.common.exception;

public class SerializationException extends Exception {

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Exception e) {
        super(e);
    }

    @Override
    public String toString() {
        return String.format("SerializationException(\"%s\")", getMessage());
    }
}

