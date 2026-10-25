package com.hwsinha.pennywise.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp,
                       int status,
                       String error,
                       String message,
                       StringBuffer path,
                       Map<String,String> validationErrors) {
}
