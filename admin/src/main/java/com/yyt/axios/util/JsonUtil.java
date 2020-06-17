package com.yyt.axios.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private JsonUtil() {}

    public static String toJson(Object target) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(target);
    }

    public static <T> T toObject(String json, T object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return (T) objectMapper.readValue(json, object.getClass());
    }
}
