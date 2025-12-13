package com.course.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "桌號訂單細項預覽資料")
public class OrderItemPreviewResponse {

	@Schema(description = "菜單名字", example = "牛排")
	private String menuName;

	@Schema(description = "訂單細項數量", example = "1")
	private Integer quantity;

}
