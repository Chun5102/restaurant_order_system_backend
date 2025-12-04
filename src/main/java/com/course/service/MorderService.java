package com.course.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.MenuEntity;
import com.course.entity.MorderEntity;
import com.course.entity.MorderItemEntity;
import com.course.model.request.MorderItemRequest;
import com.course.model.request.MorderRequest;
import com.course.model.request.MorderUpdateRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.MorderItemResponse;
import com.course.model.response.MorderResponse;
import com.course.repository.MenuRepository;
import com.course.repository.MorderItemRepository;
import com.course.repository.MorderRepository;

import jakarta.transaction.Transactional;

@Service
public class MorderService {

	@Autowired
	private MorderRepository morderRepository;

	@Autowired
	private MorderItemRepository morderItemRepository;

	@Autowired
	private MenuRepository menuRepository;

	/*
	 * 新增訂單
	 */
	@Transactional
	public ApiResponse addMorder(MorderRequest req) {
		// 檢查細項
		if (req.getMorderItem() == null || req.getMorderItem().isEmpty()) {
			return ApiResponse.error("401", "無訂單細項");
		}

		// 取出菜單價格
		List<Long> menuIdList = req.getMorderItem().stream()
				.map(MorderItemRequest::getMenuId)
				.distinct() // 去除重複
				.toList();

		Map<Long, BigDecimal> priceMap = menuRepository.findAllById(menuIdList).stream()
				.collect(Collectors.toMap(MenuEntity::getId, MenuEntity::getPrice));

		// 計算原始總價
		List<MorderItemEntity> morderItems = new ArrayList<>();
		BigDecimal calculatedBasePrice = BigDecimal.ZERO;

		for (MorderItemRequest item : req.getMorderItem()) {
			BigDecimal price = priceMap.get(item.getMenuId());
			BigDecimal calculatedSubtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
			calculatedBasePrice = calculatedBasePrice.add(calculatedSubtotal);

			// 建立訂單細項
			MorderItemEntity morderItemEntity = MorderItemEntity.builder()
					.menuId(item.getMenuId())
					.menuName(item.getMenuName())
					.quantity(item.getQuantity())
					.subtotal(calculatedSubtotal)
					.isActive(true)
					.build();

			morderItems.add(morderItemEntity);
		}

		// 取得未付款主訂單
		MorderEntity mainMorder = morderRepository.getMainMorder(req.getTableId());

		// 建立 builder，必填欄位先設定
		MorderEntity.MorderEntityBuilder builder = MorderEntity.builder()
				.code(UUID.randomUUID().toString())
				.tableId(req.getTableId())
				.basePrice(calculatedBasePrice)
				.morderStatus("待處理")
				.paymentStatus("未付款")
				.isActive(true);

		// 加點單邏輯
		List<MorderEntity> morderToSave = new ArrayList<>();
		if (mainMorder != null) {
			builder.isAddOn(true)
					.originalMorderCode(mainMorder.getCode());
			// 累加主訂單總價
			mainMorder.setTotalPrice(mainMorder.getTotalPrice().add(calculatedBasePrice));

			morderToSave.add(mainMorder);
		} else {
			builder.totalPrice(calculatedBasePrice)
					.isAddOn(false);
		}

		// 建立訂單實體並存入資料庫
		MorderEntity morderEntity = builder.build();

		morderToSave.add(morderEntity);
		morderRepository.saveAll(morderToSave);

		// 訂單細項加入Code後存入資料庫
		morderItems.forEach(item -> item.setMorderCode(morderEntity.getCode()));

		morderItemRepository.saveAll(morderItems);

		// 回傳成功
		return ApiResponse.success();
	}

	/*
	 * 更新訂單
	 * 顧客無法使用
	 */
	@Transactional
	public ApiResponse updateMorder(MorderUpdateRequest req) {
		if (morderRepository.existsByCode(req.getCode())) {
			MorderEntity morderEntity = morderRepository.findByCode(req.getCode());
			morderEntity.setMorderStatus(req.getMorderStatus());
			morderEntity.setPaymentStatus(req.getPaymentStatus());

			morderRepository.save(morderEntity);

			// if (req.getMorderItem() != null && !req.getMorderItem().isEmpty()) {
			// List<MorderItemEntity> morderItems =
			// morderItemRepository.findByMorderCode(req.getCode());
			// Map<Long, MorderItemEntity> morderItemMap = morderItems.stream()
			// .collect(Collectors.toMap(MorderItemEntity::getId, item -> item));

			// List<MorderItemEntity> itemsToDelete = morderItems.stream()
			// .filter(item -> req.getMorderItem().stream()
			// .filter(reqItem -> reqItem.getId() != null)
			// .noneMatch(reqItem -> reqItem.getId().equals(item.getId())))
			// .collect(Collectors.toList());
			// if (!itemsToDelete.isEmpty()) {
			// itemsToDelete.forEach(item -> item.setIsActive(false));
			// morderItemRepository.saveAll(itemsToDelete);
			// }

			// List<MorderItemEntity> updateMorderItem =
			// req.getMorderItem().stream().map(item -> {
			// MorderItemEntity morderItemEntity;
			// if (item.getId() != null) {
			// morderItemEntity = morderItemMap.get(item.getId());
			// morderItemEntity.setQuantity(item.getQuantity());
			// morderItemEntity.setSubtotal(item.getSubtotal());
			// morderItemEntity.setIsActive(true);
			// } else {
			// morderItemEntity = MorderItemEntity.builder()
			// .morderCode(req.getCode())
			// .menuId(item.getMenuId())
			// .menuName(item.getMenuName())
			// .quantity(item.getQuantity())
			// .subtotal(item.getSubtotal())
			// .isActive(true)
			// .build();
			// }
			// return morderItemEntity;
			// }).collect(Collectors.toList());

			// morderItemRepository.saveAll(updateMorderItem);
			// }
			return ApiResponse.success("訂單更新成功");
		} else

		{
			return ApiResponse.error("401", "無此訂單");
		}
	}

	@Transactional
	public ApiResponse deleteMorder(String code) {
		if (!morderRepository.existsByCode(code)) {
			return ApiResponse.error("401", "無此訂單");
		}

		List<MorderEntity> morderList = new ArrayList<>();
		MorderEntity morderEntity = morderRepository.findByCode(code);
		if (morderEntity.getIsAddOn()) {
			morderEntity.setIsActive(false);
			MorderEntity mainMorder = morderRepository.findByCode(morderEntity.getOriginalMorderCode());

			isTotalPriceZero(mainMorder, morderEntity.getBasePrice());
			morderList.add(morderEntity);
			morderList.add(mainMorder);
		} else {
			isTotalPriceZero(morderEntity, morderEntity.getBasePrice());
			morderList.add(morderEntity);
		}

		morderRepository.saveAll(morderList);

		List<MorderItemEntity> morderItemList = morderItemRepository.findByMorderCode(code);
		morderItemList.forEach(item -> item.setIsActive(false));

		morderItemRepository.saveAll(morderItemList);

		return ApiResponse.success("訂單刪除成功");
	}

	public ApiResponse<List<MorderResponse>> getTableNotPay(Integer tableId) {
		List<MorderEntity> morderList = morderRepository.getTableNotPay(tableId);

		List<MorderResponse> morderListRes = morderList.stream().map(morder -> {
			List<MorderItemResponse> morderItems = morderItemRepository.findByMorderCode(morder.getCode()).stream()
					.map(morderItem -> {
						MorderItemResponse morderRes = MorderItemResponse.builder()
								.Id(morderItem.getId())
								.menuId(morderItem.getMenuId())
								.menuName(morderItem.getMenuName())
								.quantity(morderItem.getQuantity())
								.subtotal(morderItem.getSubtotal())
								.build();
						return morderRes;
					}).collect(Collectors.toList());

			MorderResponse res = MorderResponse.builder()
					.code(morder.getCode())
					.tableId(morder.getTableId())
					.morderStatus(morder.getMorderStatus())
					.totalPrice(morder.getTotalPrice())
					.paymentStatus(morder.getPaymentStatus())
					.morderItem(morderItems)
					.build();
			return res;
		}).collect(Collectors.toList());
		return ApiResponse.success(morderListRes);
	}

	// 小方法放置處
	private void isTotalPriceZero(MorderEntity morder, BigDecimal subtractPrice) {
		BigDecimal newTotal = morder.getTotalPrice().subtract(subtractPrice);
		morder.setTotalPrice(newTotal);
		if (newTotal.compareTo(BigDecimal.ZERO) == 0) {
			morder.setIsActive(false);
		}
	}
}
