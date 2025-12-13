package com.course.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableStatusDto {
    @Schema(description = "桌子開始點餐")
    private boolean isOpened;
    @Schema(description = "桌子訂單數量")
    private Long orderCount;
}
