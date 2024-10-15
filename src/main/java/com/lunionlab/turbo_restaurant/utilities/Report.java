package com.lunionlab.turbo_restaurant.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.Map;
import java.util.HashMap;

public class Report {

    public static Map<String, String> getErrors(BindingResult res) {
        Map<String, String> result = new HashMap<String, String>();
        for (FieldError field : res.getFieldErrors()) {
            result.put(field.getField(), field.getDefaultMessage());
        }
        return result;
    }

    public static Object notFound(Object data) {
        return new ResponseEntity<Object>(data, HttpStatus.NOT_FOUND);
    }

    public static Object unauthorize(Object data) {
        return new ResponseEntity<Object>(data, HttpStatus.UNAUTHORIZED);
    }

    public static Object forbidden(Object data) {
        return new ResponseEntity<Object>(data, HttpStatus.FORBIDDEN);
    }

    public static Object internalError(Object data) {
        return new ResponseEntity<Object>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Map<String, Object> message(String key, Object message) {
        Map<String, Object> text = new HashMap<>();
        text.put(key, message);
        return text;
    }

    public static Map<String, Object> getMessage(String key, Object message, String code, String value) {
        Map<String, Object> text = new HashMap<>();
        text.put(key, message);
        text.put(code, value);
        return text;
    }
}
