package com.course.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.StaffEntity;
import com.course.enums.ResultCode;
import com.course.model.response.ApiResponse;
import com.course.model.vo.StaffVo;
import com.course.repository.StaffRepository;

import jakarta.transaction.Transactional;

@Service
public class StaffService {

	@Autowired
	private StaffRepository staffRepository;

	public ApiResponse<StaffVo> staffLogin(String username, String password) {
		StaffEntity staffEntity = staffRepository.findByUsernameAndPassword(username, password);
		if (staffEntity != null) {
			return ApiResponse.success(staffConvertToVoNoPassword(staffEntity));
		} else {
			return ApiResponse.error(ResultCode.LOGIN_FAIL);
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
			return ApiResponse.error(ResultCode.STAFF_IS_EXIST);
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
		return ApiResponse.error(ResultCode.STAFF_UPDATE_FAIL);
	}

	public ApiResponse<String> deleteStaff(Long id) {
		Optional<StaffEntity> staffEntityOp = staffRepository.findById(id);

		if (staffEntityOp.isPresent()) {
			staffRepository.deleteById(id);
			return ApiResponse.success("員工刪除成功");
		}
		return ApiResponse.error(ResultCode.STAFF_DELETE_FAIL);
	}

	public ApiResponse<StaffVo> staffFindById(Long id) {
		Optional<StaffEntity> staffEntityOp = staffRepository.findById(id);
		if (staffEntityOp.isPresent()) {
			return ApiResponse.success(staffConvertToVo(staffEntityOp.get()));
		}
		return ApiResponse.error(ResultCode.STAFF_NOT_EXIST);
	}

	public ApiResponse<List<StaffVo>> staffFindByName(String name) {
		List<StaffEntity> staffEntityList = staffRepository.findByNameLike("%" + name + "%");
		if (!staffEntityList.isEmpty()) {
			return ApiResponse.success(staffEntityList.stream().map(staffEntity -> {
				return staffConvertToVo(staffEntity);
			}).collect(Collectors.toList()));
		}
		return ApiResponse.error(ResultCode.STAFF_NOT_EXIST);
	}

	private StaffVo staffConvertToVo(StaffEntity staffEntity) {
		StaffVo vo = new StaffVo();
		vo.setId(staffEntity.getId());
		vo.setName(staffEntity.getName());
		vo.setUsername(staffEntity.getUsername());
		vo.setPassword(staffEntity.getPassword());
		vo.setRole(staffEntity.getRole());

		return vo;
	}

	private StaffVo staffConvertToVoNoPassword(StaffEntity staffEntity) {
		StaffVo vo = new StaffVo();
		vo.setId(staffEntity.getId());
		vo.setName(staffEntity.getName());
		vo.setUsername(staffEntity.getUsername());
		vo.setRole(staffEntity.getRole());

		return vo;
	}

}
