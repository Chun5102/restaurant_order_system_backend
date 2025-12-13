package com.course.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "上傳修改訂單")
public class OrderUpdateRequest {

	@NotNull
	@Schema(description = "訂單編號", example = "1")
	private Long Id;

	@NotNull
	@Schema(description = "訂單狀態", example = "1")
	private String orderStatus;

	@NotNull
	@Schema(description = "訂單付款狀態", example = "1")
	private String paymentStatus;

	// @NotNull
	// @Schema(description = "訂單細項")
	// private List<MorderItemUpdateRequest> morderItem = new ArrayList<>();

	// public MorderUpdateRequest() {
	// super();
	// this.morderItem = new ArrayList<>();
	// }
}
