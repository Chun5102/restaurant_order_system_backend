package com.course.model.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "訂單接收和上傳")
public class MorderResponse {

	private String code;

	private Integer tableId;

	private String morderStatus;

	private BigDecimal totalPrice;

	private String paymentStatus;

	private List<MorderItemResponse> morderItem = new ArrayList<>();

	public MorderResponse() {
		super();
		this.morderItem = new ArrayList<>();
	}

}
