package com.asankasi.dronefleet.response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.asankasi.dronefleet.util.Constants.GENERAL_ERROR_KEY;

public class CustomApiResponse {
    private final Map<String, Object> payLoad = new HashMap<>();

    public void addError(Object error) {
        payLoad.put(GENERAL_ERROR_KEY, error);
    }

    public CustomApiResponse addAttribute(String key, Object value) {
        payLoad.put(key, value);
        return this;
    }

    public Map<String, Object> getPayLoad() {
        return Collections.unmodifiableMap(payLoad);
    }
}
