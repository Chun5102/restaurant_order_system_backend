package com.course.model.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "訂單細項資料")
public class OrderItemDto {
    @Schema(description = "菜單名字", example = "牛排")
    private String menuName;

    @Schema(description = "訂單細項數量", example = "1")
    private Integer quantity;

    @Schema(description = "訂單細項小計", example = "400")
    private BigDecimal subtotal;
}
