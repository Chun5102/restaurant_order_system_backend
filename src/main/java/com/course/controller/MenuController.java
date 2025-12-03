package com.course.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.course.model.request.MenuRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.MenuManageResponse;
import com.course.model.response.MenuResponse;
import com.course.service.MenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/menu")
@Tag(name = "菜單", description = "菜單相關 API")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@Operation(summary = "新增菜單資料", tags = "菜單")
	@PostMapping(path = "/addMenu", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse addMenu(@Valid @RequestBody MenuRequest req) throws IOException {
		return menuService.addMenu(req);
	}

	@Operation(summary = "更新菜單資料", tags = "菜單")
	@PutMapping("/updateMenu/{id}")
	public ApiResponse updateMenu(@PathVariable("id") Long id, @Valid @RequestBody MenuRequest req) throws IOException {
		return menuService.updateMenu(id, req);
	}

	@Operation(summary = "刪除菜單資料", tags = "菜單")
	@PutMapping("/deleteMenu/{id}")
	public ApiResponse<String> deleteMenu(@PathVariable("id") Long id) {
		return menuService.deleteMenu(id);
	}

	@Operation(summary = "取得編輯菜單(id)", tags = "菜單")
	@GetMapping("edit/{id}")
	public ApiResponse<MenuManageResponse> getMenuById(@PathVariable("id") Long id) {
		return menuService.getMenuById(id);
	}

	@Operation(summary = "取得使用者菜單資料", tags = "菜單")
	@GetMapping("/getMenus")
	public ApiResponse<Page<MenuResponse>> getUserMenu(@RequestParam String category,
			@RequestParam Integer page) {
		return menuService.getUserMenu(category, page);
	}

	@Operation(summary = "取得管理菜單資料", tags = "菜單")
	@GetMapping("/getManageMenus")
	public ApiResponse<List<MenuManageResponse>> getManageMenu() {
		return menuService.getManageMenu();
	}

}
