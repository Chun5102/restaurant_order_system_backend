package com.course.model.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "桌子和菜單資料")
public class TableResponse {
    @Schema(description = "桌子號碼")
    private Integer id;

    @Schema(description = "桌子開啟時間")
    private LocalDateTime openedAt;

    @Schema(description = "桌子QR Code")
    private String qrCode;

}
