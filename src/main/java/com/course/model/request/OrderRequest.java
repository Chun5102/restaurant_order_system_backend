package com.course.model.request;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "上傳新增訂單")
public class OrderRequest {

	@NotNull
	@Schema(description = "訂單細項")
	private List<OrderItemRequest> orderItem = new ArrayList<>();

}
