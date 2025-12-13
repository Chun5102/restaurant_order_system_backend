package com.course.model.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "桌號訂單總資料")
public class TableOrderResponse {

	@Schema(description = "主訂單編號", example = "ffhgs1ghdhd1f...")
	private String code;

	@Schema(description = "主訂單桌號", example = "1")
	private Integer tableId;

	@Schema(description = "主訂單總價", example = "ffhgs1ghdhd1f...")
	private BigDecimal totalPrice;

	@Schema(description = "主訂單付款狀態", example = "ffhgs1ghdhd1f...")
	private String paymentStatus;

	@Schema(description = "訂單列表")
	@Builder.Default
	private List<OrderResponse> orders = new ArrayList<>();

}
