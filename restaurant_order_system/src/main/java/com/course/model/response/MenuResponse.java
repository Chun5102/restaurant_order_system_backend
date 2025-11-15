package com.course.model.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "菜單資料")
public class MenuResponse {

    @Schema(description = "菜單ID")
    private Long id;

    @Schema(description = "菜單名字")
    private String name;

    @Schema(description = "菜單類別")
    private Integer type;

    @Schema(description = "菜單價格")
    private BigDecimal price;

    @Schema(description = "菜單描述")
    private String description;

    @Schema(description = "菜單圖片 Base64 字串")
    private String imageBase64;

}
