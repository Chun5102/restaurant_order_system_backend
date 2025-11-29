package com.course.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.course.entity.MenuEntity;
import com.course.entity.StaffEntity;
import com.course.model.response.MenuManageResponse;
import com.course.model.response.MenuResponse;
import com.course.model.vo.StaffVo;

@Service
public class ServiceHelper {

	public StaffVo staffConvertToVo(StaffEntity staffEntity) {
		StaffVo vo = new StaffVo();
		vo.setId(staffEntity.getId());
		vo.setName(staffEntity.getName());
		vo.setUsername(staffEntity.getUsername());
		vo.setPassword(staffEntity.getPassword());
		vo.setRole(staffEntity.getRole());

		return vo;
	}

	public StaffVo staffConvertToVoNoPassword(StaffEntity staffEntity) {
		StaffVo vo = new StaffVo();
		vo.setId(staffEntity.getId());
		vo.setName(staffEntity.getName());
		vo.setUsername(staffEntity.getUsername());
		vo.setRole(staffEntity.getRole());

		return vo;
	}

	public MenuManageResponse menuConvertToManageResponse(MenuEntity menuEntity) {
		MenuManageResponse menuManageResponse = MenuManageResponse.builder()
				.id(menuEntity.getId())
				.name(menuEntity.getName())
				.category(menuEntity.getCategory())
				.price(menuEntity.getPrice())
				.description(menuEntity.getDescription())
				.stock(menuEntity.getStock())
				.status(menuEntity.getStatus())
				.imageBase64(generateImageBase64(menuEntity.getImageData(), menuEntity.getImageType()))
				.build();

		return menuManageResponse;
	}

	public MenuResponse menuConvertToResponse(MenuEntity menuEntity) {
		MenuResponse menuResponse = MenuResponse.builder()
				.id(menuEntity.getId())
				.name(menuEntity.getName())
				.category(menuEntity.getCategory())
				.price(menuEntity.getPrice())
				.description(menuEntity.getDescription())
				.imageBase64(generateImageBase64(menuEntity.getImageData(), menuEntity.getImageType()))
				.build();

		return menuResponse;
	}

	/**
	 * 產生圖片 Base64 字串
	 * 
	 * @param imageData 圖片資料
	 * @param imageType 圖片類型
	 * @return
	 */
	private String generateImageBase64(byte[] imageData, String imageType) {
		return imageData != null && imageType != null
				? "data:" + imageType + ";base64,"
						+ Base64.getEncoder().encodeToString(imageData)
				: null;
	}
}
