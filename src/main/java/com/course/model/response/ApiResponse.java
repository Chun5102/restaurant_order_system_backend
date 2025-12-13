package com.course.model.response;

import com.course.enums.ResultCode;

import lombok.Data;

@Data
public class ApiResponse<T> {

	private String responseCode;

	private String message;

	private T data;

	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>(ResultCode.SUCCESS, null);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(ResultCode.SUCCESS, data);
	}

	public static <T> ApiResponse<T> error(ResultCode resultCode) {
		return new ApiResponse<>(resultCode, null);
	}

	public static <T> ApiResponse<T> error(ResultCode resultCode, T data) {
		return new ApiResponse<>(resultCode, data);
	}

	private ApiResponse(ResultCode resultCode, T data) {
		super();
		this.responseCode = resultCode.getCode();
		this.message = resultCode.getMessage();
		this.data = data;
	}

	private ApiResponse() {
		super();
	}
}
