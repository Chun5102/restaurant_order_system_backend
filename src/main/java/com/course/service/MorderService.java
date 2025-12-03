package com.course.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.MorderEntity;
import com.course.entity.MorderItemEntity;
import com.course.model.request.MorderRequest;
import com.course.model.request.MorderUpdateRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.MorderItemResponse;
import com.course.model.response.MorderResponse;
import com.course.repository.MorderItemRepository;
import com.course.repository.MorderRepository;

import jakarta.transaction.Transactional;

@Service
public class MorderService {

	@Autowired
	private MorderRepository morderRepository;

	@Autowired
	private MorderItemRepository morderItemRepository;

	@Transactional
	public ApiResponse addMorder(MorderRequest req) {
		if (req.getMorderItem() == null || req.getMorderItem().isEmpty()) {
			return ApiResponse.error("401", "無訂單細項");
		}
		MorderEntity morderEntity = MorderEntity.builder()
				.code(UUID.randomUUID().toString())
				.tableId(req.getTableId())
				.totalPrice(req.getTotalPrice())
				.morderStatus("待處理")
				.paymentStatus("未付款")
				.build();

		morderRepository.save(morderEntity);

		List<MorderItemEntity> morderItems = req.getMorderItem().stream().map(item -> {
			MorderItemEntity morderItemEntity = MorderItemEntity.builder()
					.morderCode(morderEntity.getCode())
					.menuId(item.getMenuId())
					.menuName(item.getMenuName())
					.quantity(item.getQuantity())
					.subtotal(item.getSubtotal())
					.isActive(true)
					.build();

			return morderItemEntity;
		}).collect(Collectors.toList());

		morderItemRepository.saveAll(morderItems);

		return ApiResponse.success();
	}

	@Transactional
	public ApiResponse updateMorder(MorderUpdateRequest req) {
		if (morderRepository.existsByCode(req.getCode())) {
			MorderEntity morderEntity = morderRepository.findByCode(req.getCode());
			morderEntity.setMorderStatus(req.getMorderStatus());
			morderEntity.setTotalPrice(req.getTotalPrice());
			morderEntity.setPaymentStatus(req.getPaymentStatus());

			morderRepository.save(morderEntity);

			if (req.getMorderItem() != null && !req.getMorderItem().isEmpty()) {
				List<MorderItemEntity> morderItems = morderItemRepository.findByMorderCode(req.getCode());
				Map<Long, MorderItemEntity> morderItemMap = morderItems.stream()
						.collect(Collectors.toMap(MorderItemEntity::getId, item -> item));

				List<MorderItemEntity> itemsToDelete = morderItems.stream()
						.filter(item -> req.getMorderItem().stream()
								.filter(reqItem -> reqItem.getId() != null)
								.noneMatch(reqItem -> reqItem.getId().equals(item.getId())))
						.collect(Collectors.toList());
				if (!itemsToDelete.isEmpty()) {
					itemsToDelete.forEach(item -> item.setIsActive(false));
					morderItemRepository.saveAll(itemsToDelete);
				}

				List<MorderItemEntity> updateMorderItem = req.getMorderItem().stream().map(item -> {
					MorderItemEntity morderItemEntity;
					if (item.getId() != null) {
						morderItemEntity = morderItemMap.get(item.getId());
						morderItemEntity.setQuantity(item.getQuantity());
						morderItemEntity.setSubtotal(item.getSubtotal());
						morderItemEntity.setIsActive(true);
					} else {
						morderItemEntity = MorderItemEntity.builder()
								.morderCode(req.getCode())
								.menuId(item.getMenuId())
								.menuName(item.getMenuName())
								.quantity(item.getQuantity())
								.subtotal(item.getSubtotal())
								.isActive(true)
								.build();
					}
					return morderItemEntity;
				}).collect(Collectors.toList());

				morderItemRepository.saveAll(updateMorderItem);
			}
			return ApiResponse.success("訂單更新成功");
		} else

		{
			return ApiResponse.error("401", "無此訂單");
		}
	}

	@Transactional
	public ApiResponse deleteMorder(String code) {
		if (morderRepository.existsByCode(code)) {

			MorderEntity morderEntity = morderRepository.findByCode(code);
			morderEntity.setMorderStatus("0");

			morderRepository.save(morderEntity);

			List<MorderItemEntity> morderItemList = morderItemRepository.findByMorderCode(code);
			morderItemList.forEach(item -> item.setIsActive(false));

			morderItemRepository.saveAll(morderItemList);

			return ApiResponse.success("訂單刪除成功");
		} else {
			return ApiResponse.error("401", "無此訂單");
		}
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

}
