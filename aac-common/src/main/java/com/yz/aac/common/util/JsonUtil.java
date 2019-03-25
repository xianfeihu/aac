package com.yz.aac.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yz.aac.common.exception.SerializationException;

@SuppressWarnings("unused")
public final class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToBean(String json, Class<T> clazz) throws SerializationException {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonString, Class<T> itemClass) throws SerializationException {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, itemClass);
        try {
            return (List<T>) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static String beanToJson(Object bean) throws SerializationException {
        try {
            return mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
	public static HashMap<String,Object> jsonToMap(String json) throws SerializationException {
        try {
        	return mapper.readValue(json,HashMap.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

}
