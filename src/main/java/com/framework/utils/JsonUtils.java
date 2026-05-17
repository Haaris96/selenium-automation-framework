package com.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Jackson-based JSON utility for reading test data from JSON files.
 */
public final class JsonUtils {

    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {}

    public static <T> T readJsonFile(String filePath, Class<T> clazz) {
        try {
            T obj = mapper.readValue(new File(filePath), clazz);
            log.info("Read JSON file: {}", filePath);
            return obj;
        } catch (IOException e) {
            log.error("Failed to read JSON file: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> readJsonAsList(String filePath) {
        try {
            List<Map<String, Object>> list = mapper.readValue(
                new File(filePath), new TypeReference<>() {});
            log.info("Read {} records from JSON: {}", list.size(), filePath);
            return list;
        } catch (IOException e) {
            log.error("Failed to read JSON list from: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> readJsonAsMap(String filePath) {
        try {
            return mapper.readValue(new File(filePath), new TypeReference<>() {});
        } catch (IOException e) {
            log.error("Failed to read JSON map from: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.error("Failed to serialize object to JSON", e);
            return "{}";
        }
    }
}
