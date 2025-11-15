package com.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.model.request.MorderRequest;
import com.course.model.response.ApiResponse;
import com.course.model.vo.MorderVo;
import com.course.service.MorderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "訂單相關 API")
public class MorderController {

	@Autowired
	private MorderService morderService;

	@Operation(summary = "新增訂單資料", tags = "訂單")
	@PostMapping("/add")
	public ApiResponse<String> addMorder(@Valid @RequestBody MorderRequest req) {
		return morderService.addMorder(req);
	}

	@Operation(summary = "更新訂單資料", tags = "訂單")
	@PostMapping("/update")
	public ApiResponse<String> updateMorder(@RequestBody MorderVo vo) {
		return morderService.updateMorder(vo);
	}

	@Operation(summary = "刪除訂單資料", tags = "訂單")
	@PostMapping("/delete/{code}")
	public ApiResponse<String> deleteMorder(@PathVariable String code) {
		return morderService.deleteMorder(code);
	}

	@Operation(summary = "搜索訂單(tableNumber)", tags = "訂單")
	@GetMapping("/select-tableNumber/{tableNumber}")
	public ApiResponse<List<MorderVo>> findByType(@PathVariable Integer tableNumber) {
		return morderService.findByTableNum(tableNumber);
	}
}
