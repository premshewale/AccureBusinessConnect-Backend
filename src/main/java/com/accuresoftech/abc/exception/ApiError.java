package com.accuresoftech.abc.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private String debugMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}
