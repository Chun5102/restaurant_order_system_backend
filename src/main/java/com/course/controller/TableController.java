package com.course.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.annotation.RequireTableToken;
import com.course.model.request.TableRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.TableResponse;
import com.course.model.response.TableStatusResponse;
import com.course.service.TableService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/table")
@Tag(name = "桌子", description = "桌子相關 API")
public class TableController {
    @Autowired
    private TableService tableService;

    @Operation(summary = "新增桌子資料", tags = "桌子")
    @PostMapping(path = "/addTable")
    public ApiResponse<Object> addTable() {
        return tableService.addTable();
    }

    @Operation(summary = "更新桌子資料", tags = "桌子")
    @PutMapping(path = "/updateTable/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Object> updateTable(@PathVariable("id") Integer id, @Valid @RequestBody TableRequest req)
            throws IOException {
        return tableService.updateTable(id, req);
    }

    @Operation(summary = "開桌", tags = "桌子")
    @PostMapping(path = "/openTable/{code}")
    public ApiResponse<String> openTable(@PathVariable("code") String code)
            throws IOException {
        return tableService.openTable(code);
    }

    @Operation(summary = "檢查桌子資料", tags = "桌子")
    @RequireTableToken(validateOpenedAt = true)
    @GetMapping(path = "/checkTable")
    public ApiResponse<Object> checkTable(HttpServletRequest request) {
        Integer tableId = (Integer) request.getAttribute("tableId");
        LocalDateTime openedAt = (LocalDateTime) request.getAttribute("openedAt");
        return tableService.checkTable(tableId, openedAt);
    }

    @Operation(summary = "取得桌子資料", tags = "桌子")
    @GetMapping(path = "/getTable/{code}")
    public ApiResponse<TableResponse> getTable(@PathVariable("code") String code)
            throws IOException {
        return tableService.getTable(code);
    }

    @Operation(summary = "取得桌子狀態", tags = "桌子")
    @RequireTableToken
    @GetMapping(path = "/getTableStatus")
    public ApiResponse<TableStatusResponse> getTableStatus(HttpServletRequest request) {
        Integer tableId = (Integer) request.getAttribute("tableId");
        return tableService.getTableStatus(tableId);
    }
}
