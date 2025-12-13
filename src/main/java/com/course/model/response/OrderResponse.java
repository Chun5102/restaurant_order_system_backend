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
@Schema(description = "桌號訂單列表資料")
public class OrderResponse {
    @Schema(description = "訂單編號", example = "1")
    private Long id;

    @Schema(description = "訂單總價", example = "ffhgs1ghdhd1f...")
    private BigDecimal totalPrice;

    @Schema(description = "訂單狀態", example = "ffhgs1ghdhd1f...")
    private String orderStatus;

    @Schema(description = "訂單細項預覽列表")
    @Builder.Default
    private List<OrderItemPreviewResponse> orderItems = new ArrayList<>();
}
