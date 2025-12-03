package com.course.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.model.request.TableRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.TableResponse;
import com.course.service.TableService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/table")
@Tag(name = "桌子", description = "桌子相關 API")
public class TableController {
    @Autowired
    private TableService tableService;

    @Operation(summary = "新增桌子資料", tags = "桌子")
    @PostMapping(path = "/addTable")
    public ApiResponse addTable() {
        return tableService.addTable();
    }

    @Operation(summary = "更新桌子資料", tags = "桌子")
    @PutMapping(path = "/updateTable/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateTable(@PathVariable("id") Integer id, @Valid @RequestBody TableRequest req)
            throws IOException {
        return tableService.updateTable(id, req);
    }

    @Operation(summary = "取得桌子資料", tags = "桌子")
    @PutMapping(path = "/openTable/{code}")
    public ApiResponse<TableResponse> openTable(@PathVariable("code") String code)
            throws IOException {
        return tableService.openTable(code);
    }

    @Operation(summary = "取得桌子資料", tags = "桌子")
    @GetMapping(path = "/getTable/{code}")
    public ApiResponse getTable(@PathVariable("code") String code)
            throws IOException {
        return tableService.getTable(code);
    }
}
