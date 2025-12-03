package com.accuresoftech.abc.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
		Map<String, Object> body = Map.of("status", HttpStatus.NOT_FOUND.name(), "message", ex.getMessage(),
				"timestamp", LocalDateTime.now().toString());
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}



	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
		Map<String, Object> body = Map.of("status", HttpStatus.FORBIDDEN.name(), "message", "Access denied",
				"timestamp", LocalDateTime.now().toString());
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining("; "));
		Map<String, Object> body = Map.of("status", HttpStatus.BAD_REQUEST.name(), "message",
				"Validation failed: " + message, "timestamp", LocalDateTime.now().toString());
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleAll(Exception ex) {
		Map<String, Object> body = Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.name(), "message", ex.getMessage(),
				"timestamp", LocalDateTime.now().toString());
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflict(ConflictException ex) {
        ApiError err = new ApiError(HttpStatus.CONFLICT);
        err.setMessage(ex.getMessage());
        err.setDebugMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(err, new HttpHeaders(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        ApiError err = new ApiError(HttpStatus.BAD_REQUEST);
        err.setMessage(ex.getMessage());
        err.setDebugMessage(ex.getMessage());
        err.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(err, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
