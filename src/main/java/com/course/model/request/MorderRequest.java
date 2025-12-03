package com.course.model.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "上傳新增訂單")
public class MorderRequest {

	@NotNull
	@Schema(description = "訂單桌號", example = "1")
	private Integer tableId;

	@NotNull
	@Schema(description = "訂單總價", example = "999")
	private BigDecimal totalPrice;

	@NotNull
	@Schema(description = "訂單細項")
	private List<MorderItemRequest> morderItem = new ArrayList<>();

	public MorderRequest() {
		super();
		this.morderItem = new ArrayList<>();
	}
}
