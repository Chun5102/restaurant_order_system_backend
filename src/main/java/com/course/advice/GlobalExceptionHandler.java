package com.course.advice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.course.model.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ApiResponse<Map<String, String>> exceptionHandler(MethodArgumentNotValidException e) {
		
		logger.error("參數驗證錯誤!!!!", e);
		
        Map<String, String> errorMap = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
        	System.out.println(fieldError.getDefaultMessage());
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        
        ApiResponse<Map<String, String>> response = ApiResponse.error("401", "參數驗證錯誤", errorMap);

        return response;
	}
	
	@ExceptionHandler(value = Exception.class)
	public ApiResponse<Map<String, String>> allExceptionHandler(Exception e) {
		
        ApiResponse<Map<String, String>> response = ApiResponse.error("505", "系統忙碌中", null);

        return response;
	}
}
