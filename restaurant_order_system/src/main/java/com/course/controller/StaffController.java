package com.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.course.model.response.ApiResponse;
import com.course.model.vo.StaffVo;
import com.course.service.StaffService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/staff")
public class StaffController {

	@Autowired
	private StaffService staffService;

	@Operation(summary = "員工登入", tags = "員工")
	@PostMapping("/login")
	public ApiResponse<StaffVo> staffLogin(@RequestParam String username, @RequestParam String password) {
		return staffService.staffLogin(username, password);
	}

	@Operation(summary = "新增員工資料", tags = "員工")
	@PostMapping("/add")
	public ApiResponse<String> addStaff(@Valid @RequestBody StaffVo vo) {
		return staffService.addStaff(vo);
	}

	@Operation(summary = "更新員工資料", tags = "員工")
	@PostMapping("/update")
	public ApiResponse<String> updateStaff(@Valid @RequestBody StaffVo vo) {
		return staffService.updateStaff(vo);
	}

	@Operation(summary = "刪除員工", tags = "員工")
	@PostMapping("/delete/{id}")
	public ApiResponse<String> deleteStaff(@PathVariable Long id) {
		return staffService.deleteStaff(id);
	}

	@Operation(summary = "搜索員工(id)", tags = "員工")
	@GetMapping("/select/{id}")
	public ApiResponse<StaffVo> findById(@PathVariable Long id) {
		return staffService.staffFindById(id);
	}

	@Operation(summary = "搜索員工(name)", tags = "員工")
	@GetMapping("/select-like/{name}")
	public ApiResponse<List<StaffVo>> findByName(@PathVariable String name) {
		return staffService.staffFindByName(name);
	}
}
