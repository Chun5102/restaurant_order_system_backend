package com.course.model.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "訂單細項新增上傳")
public class MorderItemRequest {

	@NotNull
	@Schema(description = "菜單ID", example = "1")
	private Long menuId;

	@NotNull
	@Schema(description = "菜單名字", example = "牛排")
	private String menuName;

	@NotNull
	@Schema(description = "訂單細項數量", example = "1")
	private Integer quantity;

	@NotNull
	@Schema(description = "訂單細項小計", example = "400")
	private BigDecimal subtotal;
}
