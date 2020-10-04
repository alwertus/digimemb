package ru.alwertus.digimemb.rest;

import org.json.JSONObject;

public interface AddingError {
    default String addError(JSONObject json, String errorText) {
        json.put("Result", "Error");
        json.put("Error", errorText);
        return errorText;
    }
}
