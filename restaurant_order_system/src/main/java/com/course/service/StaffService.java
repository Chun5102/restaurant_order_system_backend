package com.course.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.StaffEntity;
import com.course.model.response.ApiResponse;
import com.course.model.vo.StaffVo;
import com.course.repository.StaffRepository;

import jakarta.transaction.Transactional;

@Service
public class StaffService {

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private ServiceHelper helper;

	public ApiResponse<StaffVo> staffLogin(String username, String password) {
		StaffEntity staffEntity = staffRepository.findByUsernameAndPassword(username, password);
		if (staffEntity != null) {
			return ApiResponse.success(helper.staffConvertToVoNoPassword(staffEntity));
		} else {
			return ApiResponse.error("401", "登入失敗");
		}
	}

	@Transactional
	public ApiResponse<String> addStaff(StaffVo vo) {
		if (!staffRepository.existsByUsername(vo.getUsername())) {
			StaffEntity staffEntity = new StaffEntity();
			staffEntity.setName(vo.getName());
			staffEntity.setUsername(vo.getUsername());
			staffEntity.setPassword(vo.getPassword());
			staffEntity.setRole(vo.getRole());

			staffRepository.save(staffEntity);

			return ApiResponse.success("員工新增成功");
		} else {
			return ApiResponse.error("401", "已有此帳號");
		}
	}

	@Transactional
	public ApiResponse<String> updateStaff(StaffVo vo) {
		Optional<StaffEntity> staffEntityOp = staffRepository.findById(vo.getId());
		if (staffEntityOp.isPresent()) {
			StaffEntity staffEntity = staffEntityOp.get();
			staffEntity.setName(vo.getName());
			staffEntity.setPassword(vo.getPassword());
			staffEntity.setRole(vo.getRole());

			staffRepository.save(staffEntity);
			return ApiResponse.success("員工修改成功");
		}
		return ApiResponse.error("401", "修改失敗");
	}

	public ApiResponse<String> deleteStaff(Long id) {
		Optional<StaffEntity> staffEntityOp = staffRepository.findById(id);

		if (staffEntityOp.isPresent()) {
			staffRepository.deleteById(id);
			return ApiResponse.success("員工刪除成功");
		}
		return ApiResponse.error("401", "刪除失敗");
	}

	public ApiResponse<StaffVo> staffFindById(Long id) {
		Optional<StaffEntity> staffEntityOp = staffRepository.findById(id);
		if (staffEntityOp.isPresent()) {
			return ApiResponse.success(helper.staffConvertToVo(staffEntityOp.get()));
		}
		return ApiResponse.error("401", "搜索失敗");
	}

	public ApiResponse<List<StaffVo>> staffFindByName(String name) {
		List<StaffEntity> staffEntityList = staffRepository.findByNameLike("%" + name + "%");
		if (!staffEntityList.isEmpty()) {
			return ApiResponse.success(staffEntityList.stream().map(staffEntity -> {
				return helper.staffConvertToVo(staffEntity);
			}).collect(Collectors.toList()));
		}
		return ApiResponse.error("401", "搜索失敗");
	}
}
