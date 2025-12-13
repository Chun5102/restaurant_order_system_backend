package com.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.annotation.RequireTableToken;
import com.course.model.request.OrderRequest;
import com.course.model.request.OrderUpdateRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.TableOrderResponse;
import com.course.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/order")
@Tag(name = "訂單", description = "訂單相關 API")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Operation(summary = "新增訂單資料", tags = "訂單")
	@RequireTableToken
	@PostMapping("/addOrder")
	public ApiResponse<Object> addOrder(@Valid @RequestBody OrderRequest req, HttpServletRequest request) {
		Integer tableId = (Integer) request.getAttribute("tableId");
		return orderService.addOrder(req, tableId);
	}

	@Operation(summary = "更新訂單資料", tags = "訂單")
	@PostMapping("/updateOrder")
	public ApiResponse<Object> updateOrder(@Valid @RequestBody OrderUpdateRequest req) {
		return orderService.updateOrder(req);
	}

	@Operation(summary = "刪除訂單資料", tags = "訂單")
	@RequireTableToken
	@PostMapping("/deleteOrder/{id}")
	public ApiResponse<TableOrderResponse> deleteOrder(@PathVariable Long id, HttpServletRequest request) {
		Integer tableId = (Integer) request.getAttribute("tableId");
		return orderService.deleteOrder(id, tableId);
	}

	@Operation(summary = "取得未付款訂單", tags = "訂單")
	@GetMapping("/getTableOrder")
	public ApiResponse<TableOrderResponse> getTableNotPayOrder(HttpServletRequest request) {
		Integer tableId = (Integer) request.getAttribute("tableId");
		return orderService.getTableNotPayOrder(tableId);
	}

}
