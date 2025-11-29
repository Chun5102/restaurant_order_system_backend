package com.course.model.response;

import lombok.Data;

@Data
public class ApiResponse<T> {

	private String responseCode;

	private String message;

	private T data;

	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>("200", "成功", null);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>("200", "成功", data);
	}

	public static <T> ApiResponse<T> error(String code, String message) {
		return new ApiResponse<>(code, message, null);
	}

	public static <T> ApiResponse<T> error(String code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}

	public ApiResponse(String resposeCode, String message, T data) {
		super();
		this.responseCode = resposeCode;
		this.message = message;
		this.data = data;
	}

	public ApiResponse() {
		super();
	}
}
