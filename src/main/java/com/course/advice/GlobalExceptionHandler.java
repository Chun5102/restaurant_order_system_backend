package com.course.advice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.course.enums.ResultCode;
import com.course.model.response.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ApiResponse<Map<String, String>> exceptionHandler(MethodArgumentNotValidException e) {

		logger.error("參數驗證錯誤!!!!", e);

		Map<String, String> errorMap = new HashMap<>();

		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			System.out.println(fieldError.getDefaultMessage());
			errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		ApiResponse<Map<String, String>> response = ApiResponse.error(ResultCode.INVALID_ARGUMENTS, errorMap);

		return response;
	}

	@ExceptionHandler(JwtException.class)
	public ApiResponse<Object> handleJwtException(JwtException e) {
		if (e instanceof ExpiredJwtException) {
			return ApiResponse.error(ResultCode.TABLE_TOKEN_EXPIRED);
		} else if (e instanceof MalformedJwtException) {
			return ApiResponse.error(ResultCode.TABLE_TOKEN_INVALID);
		} else if (e.getMessage().equals(ResultCode.TABLE_TOKEN_MISSING.name())) {
			return ApiResponse.error(ResultCode.TABLE_TOKEN_MISSING);
		}

		return ApiResponse.error(ResultCode.TABLE_TOKEN_INTERNAL_ERROR);
	}

	@ExceptionHandler(SecurityException.class)
	public ApiResponse<Object> handleJwtException(SecurityException e) {
		return ApiResponse.error(ResultCode.TABLE_TOKEN_INVALID);
	}

	@ExceptionHandler(value = Exception.class)
	public ApiResponse<Map<String, String>> allExceptionHandler(Exception e) {

		ApiResponse<Map<String, String>> response = ApiResponse.error(ResultCode.FAIL);

		return response;
	}
}
