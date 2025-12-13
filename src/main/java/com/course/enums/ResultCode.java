package com.course.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // 共用
    SUCCESS("200", "成功"),
    INVALID_ARGUMENTS("400", "參數驗證錯誤"),
    FAIL("500", "系統忙碌中,請稍後再試"),
    // 員工
    LOGIN_FAIL("1000", "帳號或密碼錯誤"),
    STAFF_IS_EXIST("1001", "員工已存在"),
    STAFF_UPDATE_FAIL("1002", "員工資料更新失敗"),
    STAFF_DELETE_FAIL("1003", "員工刪除失敗"),
    STAFF_NOT_EXIST("1004", "員工不存在"),
    // 菜單
    MENU_IS_EXIST("2000", "菜單已存在"),
    MENU_NOT_EXIST("2001", "菜單不存在"),
    // 訂單
    ORDER_ITEM_IS_EMPTY("3000", "該訂單沒有菜單項目"),
    ORDER_NOT_EXIST("3001", "訂單資料不存在"),
    ORDER_DATA_INVALID("3002", "訂單資料異常或缺失"),
    MAIN_ORDER_NOT_EXIST("3003", "主訂單資料不存在"),
    NOT_OWN_TABLE_ORDER("3004", "非本桌訂單資料"),
    // 桌子
    TABLE_NOT_EXIST("4000", "桌子不存在"),
    TABLE_TOKEN_EXPIRED("4001", "桌子token已過期"),
    TABLE_TOKEN_INVALID("4002", "桌子token無效"),
    TABLE_TOKEN_MISSING("4003", "桌子token缺失"),
    TABLE_TOKEN_INTERNAL_ERROR("4004", "桌子token錯誤"),
    NOT_SAME_GUEST("4005", "不是同一位客人"),
    ;

    private final String code;
    private final String message;
}
