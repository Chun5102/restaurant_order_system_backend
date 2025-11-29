package com.course.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuStatus {
    ONSALE(2, "銷售中"),
    STOPED(1, "停售"),
    DELETE(0, "刪除");

    private final Integer code;
    private final String desc;

    // 取得產品狀態描述
    public static String getDesc(Integer code) {
        for (MenuStatus menuStatus : MenuStatus.values()) {
            if (menuStatus.getCode().equals(code)) {
                return menuStatus.getDesc();
            }
        }
        return "無此狀態";
    }
}
