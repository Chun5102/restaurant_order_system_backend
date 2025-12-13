package com.course.model.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TableCheckRequest {

    @Schema(description = "桌子號碼")
    private Integer id;

    @Schema(description = "桌子開啟時間")
    private LocalDateTime openedAt;

    @Schema(description = "桌子到期時間")
    private LocalDateTime expiryTime;

    @Schema(description = "桌子QR Code")
    private String qrCode;
}
