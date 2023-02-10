package com.me.springdata.mongodb.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.me.springdata.mongodb.exception.JsonParseException;

import java.io.IOException;

public class JsonUtils {

    private static ObjectMapper mapper;

    private static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        return mapper;
    }

    public static String toJson(Object target) {
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(target);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonParseException(target.toString(), e);
        }
    }
}
