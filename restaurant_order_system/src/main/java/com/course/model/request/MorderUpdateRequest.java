package com.course.model.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.course.model.vo.MorderItemVo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "上傳修改訂單")
public class MorderUpdateRequest {

	@NotNull
	@Schema(description = "訂單自訂編號", example = "550e8400-e29b-41d4-a716-446655440000")
	private String code;

	@NotNull
	@Schema(description = "訂單狀態", example = "1")
	private String morderStatus;

	@NotNull
	@Schema(description = "訂單總價", example = "999")
	private BigDecimal totalPrice;

	@NotNull
	@Schema(description = "訂單付款狀態", example = "1")
	private String paymentStatus;

	@NotNull
	@Schema(description = "訂單細項")
	private List<MorderItemVo> morderItem = new ArrayList<>();

	public MorderUpdateRequest() {
		super();
		this.morderItem = new ArrayList<>();
	}
}
