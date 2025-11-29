package com.course.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StaffVo {

	private Long id;
	@NotBlank
	@NotNull
	private String name;

	@NotBlank
	@NotNull
	private String username;

	@NotBlank
	@NotNull
	private String password;

	@NotBlank
	@NotNull
	private String role;
}
