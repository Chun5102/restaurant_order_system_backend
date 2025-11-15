package com.course.model.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "菜單上傳")
public class MenuRequest {

    @NotBlank
    @NotNull
    @Schema(description = "菜單名字", example = "牛排")
    private String name;

    @NotNull
    @Schema(description = "菜單類別", example = "主食")
    private Integer type;

    @NotNull
    @Schema(description = "菜單價格", example = "400")
    private BigDecimal price;

    @NotNull
    @Schema(description = "菜單描述", example = "先嫩多汁")
    private String description;

    @NotNull
    @Schema(description = "菜單庫存", example = "10")
    private Integer stock;

    @NotNull
    @Schema(description = "菜單狀態", example = "0")
    private Integer status;

    @Schema(description = "菜單圖片 Base64 字串", example = "iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageBase64;

    @Schema(description = "菜單圖片格式", example = "image/png")
    private String imageType;
}
