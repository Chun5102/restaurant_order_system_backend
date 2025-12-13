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
@Schema(description = "桌子狀態資料")
public class TableStatusResponse {

    @Schema(description = "桌子開始點餐")
    private boolean isOpened;
    @Schema(description = "桌子訂單數量")
    private Long orderCount;
}
