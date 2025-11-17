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
import com.course.model.response.MorderResponse;
import com.course.model.vo.MorderItemVo;
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
		if (req.getMorderItem() != null || !req.getMorderItem().isEmpty()) {

			MorderEntity morderEntity = MorderEntity.builder()
					.code(UUID.randomUUID().toString())
					.tableNumber(req.getTableNumber())
					.totalPrice(req.getTotalPrice())
					.morderStatus(req.getMorderStatus())
					.paymentStatus(req.getPaymentStatus())
					.build();

			morderRepository.save(morderEntity);

			List<MorderItemEntity> morderItems = req.getMorderItem().stream().map(item -> {
				MorderItemEntity morderItemEntity = MorderItemEntity.builder()
						.morderCode(morderEntity.getCode())
						.menuId(item.getMenuId())
						.quantity(item.getQuantity())
						.subtotal(item.getSubtotal())
						.build();

				return morderItemEntity;
			}).collect(Collectors.toList());

			morderItemRepository.saveAll(morderItems);

			return ApiResponse.success();
		} else {
			return ApiResponse.error("401", "無訂單細項");
		}
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
						.collect(Collectors.toMap(MorderItemEntity::getMenuId, item -> item));

				List<MorderItemEntity> itemsToDelete = morderItems.stream()
						.filter(item -> req.getMorderItem().stream()
								.noneMatch(reqItem -> reqItem.getMenuId().equals(item.getMenuId())))
						.collect(Collectors.toList());
				if (!itemsToDelete.isEmpty()) {
					morderItemRepository.deleteAllInBatch(itemsToDelete);
				}

				List<MorderItemEntity> updateMorderItem = req.getMorderItem().stream().map(item -> {
					MorderItemEntity morderItemEntity = morderItemMap.get(item.getMenuId());
					if (morderItemEntity == null) {
						morderItemEntity = new MorderItemEntity();
						morderItemEntity.setMenuId(item.getMenuId());
						morderItemEntity.setMorderCode(req.getCode());
					}
					morderItemEntity.setQuantity(item.getQuantity());
					morderItemEntity.setSubtotal(item.getSubtotal());
					return morderItemEntity;
				}).collect(Collectors.toList());

				morderItemRepository.saveAll(updateMorderItem);
			}
			return ApiResponse.success("訂單更新成功");
		} else {
			return ApiResponse.error("401", "無此訂單");
		}
	}

	@Transactional
	public ApiResponse deleteMorder(String code) {
		if (morderRepository.existsByCode(code)) {

			morderRepository.deleteByCode(code);

			List<MorderItemEntity> morderItemList = morderItemRepository.findByMorderCode(code);
			morderItemRepository.deleteAllInBatch(morderItemList);
			return ApiResponse.success("訂單刪除成功");
		} else {
			return ApiResponse.error("401", "無此訂單");
		}
	}

	public ApiResponse<List<MorderResponse>> getTableNumNotPay(Integer tableNum) {
		List<MorderEntity> morderList = morderRepository.getTableNumNotPay(tableNum);

		List<MorderResponse> resList = morderList.stream().map(morder -> {
			List<MorderItemVo> morderItems = morderItemRepository.findByMorderCodeToVo(morder.getCode());

			MorderResponse res = MorderResponse.builder()
					.code(morder.getCode())
					.tableNumber(morder.getTableNumber())
					.morderStatus(morder.getMorderStatus())
					.totalPrice(morder.getTotalPrice())
					.paymentStatus(morder.getPaymentStatus())
					.morderItem(morderItems)
					.build();
			return res;
		}).collect(Collectors.toList());
		return ApiResponse.success(resList);
	}

}
