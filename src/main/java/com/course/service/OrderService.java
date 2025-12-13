package com.course.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.MainOrderEntity;
import com.course.entity.MenuEntity;
import com.course.entity.OrderEntity;
import com.course.entity.OrderItemEntity;
import com.course.entity.TableEntity;
import com.course.enums.ResultCode;
import com.course.model.request.OrderItemRequest;
import com.course.model.request.OrderRequest;
import com.course.model.request.OrderUpdateRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.OrderItemPreviewResponse;
import com.course.model.response.OrderResponse;
import com.course.model.response.TableOrderResponse;
import com.course.repository.MainOrderRepository;
import com.course.repository.MenuRepository;
import com.course.repository.OrderItemRepository;
import com.course.repository.OrderRepository;
import com.course.repository.TableRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private MainOrderRepository mainOrderRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private TableRepository tableRepository;

	@Autowired
	private MenuRepository menuRepository;

	/*
	 * 新增訂單
	 */
	@Transactional
	public ApiResponse<Object> addOrder(OrderRequest req, Integer tableId) {
		// 檢查細項
		if (req.getOrderItem() == null || req.getOrderItem().isEmpty()) {
			return ApiResponse.error(ResultCode.ORDER_ITEM_IS_EMPTY);
		}

		// 取出菜單價格
		List<Long> menuIdList = req.getOrderItem().stream()
				.map(OrderItemRequest::getMenuId)
				.distinct() // 去除重複
				.toList();

		Map<Long, BigDecimal> priceMap = menuRepository.findAllById(menuIdList).stream()
				.collect(Collectors.toMap(MenuEntity::getId, MenuEntity::getPrice));

		// 計算原始總價
		List<OrderItemEntity> orderItems = new ArrayList<>();
		BigDecimal calculatedTotalPrice = BigDecimal.ZERO;

		for (OrderItemRequest item : req.getOrderItem()) {
			BigDecimal price = priceMap.get(item.getMenuId());
			BigDecimal calculatedSubtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
			calculatedTotalPrice = calculatedTotalPrice.add(calculatedSubtotal);

			// 建立訂單細項
			OrderItemEntity orderItemEntity = OrderItemEntity.builder()
					.menuId(item.getMenuId())
					.menuName(item.getMenuName())
					.quantity(item.getQuantity())
					.subtotal(calculatedSubtotal)
					.isActive(true)
					.build();

			orderItems.add(orderItemEntity);
		}

		/*
		 * 取得使用中的桌子
		 * 防止主訂單同時新增
		 */
		TableEntity tableEntity = tableRepository.findByIdAndStatus(tableId, "使用中");

		// 取得未付款主訂單
		MainOrderEntity mainOrder = mainOrderRepository.getMainOrder(tableId);

		// 建立 builder，必填欄位先設定
		OrderEntity.OrderEntityBuilder builder = OrderEntity.builder()
				.totalPrice(calculatedTotalPrice)
				.orderStatus("待處理")
				.isActive(true);

		if (mainOrder != null) {
			builder.mainOrderCode(mainOrder.getCode());
			// 累加主訂單總價
			mainOrder.setTotalPrice(mainOrder.getTotalPrice().add(calculatedTotalPrice));
			mainOrderRepository.save(mainOrder);
		} else {
			// 建立主訂單
			String mainOrderCode = UUID.randomUUID().toString();
			mainOrder = MainOrderEntity.builder()
					.code(mainOrderCode)
					.totalPrice(calculatedTotalPrice)
					.mainOrderStatus("已建立")
					.paymentStatus("未付款")
					.isActive(true)
					.tableId(tableId)
					.build();

			mainOrderRepository.save(mainOrder);

			builder.mainOrderCode(mainOrderCode);

			// 更新桌子狀態
			tableEntity.setStatus("點餐");
			tableRepository.save(tableEntity);
		}

		// 建立訂單實體並存入資料庫
		OrderEntity orderEntity = builder.build();

		orderRepository.save(orderEntity);

		// 訂單細項加入Code後存入資料庫
		orderItems.forEach(item -> item.setOrderId(orderEntity.getId()));

		orderItemRepository.saveAll(orderItems);

		// 回傳成功
		return ApiResponse.success();
	}

	/*
	 * 更新訂單
	 * 顧客無法使用
	 */
	@Transactional
	public ApiResponse<Object> updateOrder(OrderUpdateRequest req) {
		OrderEntity orderEntity = orderRepository.findById(req.getId()).orElse(null);
		if (orderEntity == null) {
			return ApiResponse.error(ResultCode.ORDER_NOT_EXIST);
		}
		orderEntity.setOrderStatus(req.getOrderStatus());

		orderRepository.save(orderEntity);

		// if (req.getOrderItem() != null && !req.getOrderItem().isEmpty()) {
		// List<OrderItemEntity> orderItems =
		// orderItemRepository.findByOrderCode(req.getCode());
		// Map<Long, OrderItemEntity> orderItemMap = orderItems.stream()
		// .collect(Collectors.toMap(OrderItemEntity::getId, item -> item));

		// List<OrderItemEntity> itemsToDelete = orderItems.stream()
		// .filter(item -> req.getOrderItem().stream()
		// .filter(reqItem -> reqItem.getId() != null)
		// .noneMatch(reqItem -> reqItem.getId().equals(item.getId())))
		// .collect(Collectors.toList());
		// if (!itemsToDelete.isEmpty()) {
		// itemsToDelete.forEach(item -> item.setIsActive(false));
		// orderItemRepository.saveAll(itemsToDelete);
		// }

		// List<OrderItemEntity> updateOrderItem =
		// req.getOrderItem().stream().map(item -> {
		// OrderItemEntity OrderItemEntity;
		// if (item.getId() != null) {
		// OrderItemEntity = orderItemMap.get(item.getId());
		// OrderItemEntity.setQuantity(item.getQuantity());
		// OrderItemEntity.setSubtotal(item.getSubtotal());
		// OrderItemEntity.setIsActive(true);
		// } else {
		// OrderItemEntity = OrderItemEntity.builder()
		// .orderCode(req.getCode())
		// .menuId(item.getMenuId())
		// .menuName(item.getMenuName())
		// .quantity(item.getQuantity())
		// .subtotal(item.getSubtotal())
		// .isActive(true)
		// .build();
		// }
		// return OrderItemEntity;
		// }).collect(Collectors.toList());

		// orderItemRepository.saveAll(updateOrderItem);
		// }
		return ApiResponse.success("訂單更新成功");
	}

	@Transactional
	public ApiResponse<TableOrderResponse> deleteOrder(Long id, Integer tableId) {
		// 判斷訂單是否存在
		OrderEntity orderEntity = orderRepository.findById(id).orElse(null);
		if (orderEntity == null) {
			return ApiResponse.error(ResultCode.ORDER_NOT_EXIST);
		}
		// 判斷主訂單是否存在
		MainOrderEntity mainOrder = mainOrderRepository.findByCode(orderEntity.getMainOrderCode());

		if (mainOrder == null) {
			return ApiResponse.error(ResultCode.MAIN_ORDER_NOT_EXIST);
		}

		// 判斷是否為該桌的訂單
		if (!mainOrder.getTableId().equals(tableId)) {
			return ApiResponse.error(ResultCode.NOT_OWN_TABLE_ORDER);
		}

		// 刪除訂單
		orderEntity.setIsActive(false);
		orderRepository.save(orderEntity);

		List<OrderItemEntity> orderItemList = orderItemRepository.findByOrderId(id);
		orderItemList.forEach(item -> item.setIsActive(false));

		orderItemRepository.saveAll(orderItemList);

		// 累加主訂單總價
		if (mainOrder.getTotalPrice() == null || orderEntity.getTotalPrice() == null) {
			return ApiResponse.error(ResultCode.ORDER_DATA_INVALID);
		}
		mainOrder.setTotalPrice(mainOrder.getTotalPrice().subtract(orderEntity.getTotalPrice()));

		// 判斷主訂單是否刪除
		Boolean hasActiveOrder = orderRepository.existsByMainOrderCodeAndIsActive(mainOrder.getCode(), true);
		if (!hasActiveOrder) {
			mainOrder.setIsActive(false);

			TableEntity table = tableRepository.findById(tableId).orElse(null);
			table.setStatus("使用中");
			tableRepository.save(table);
		}

		mainOrderRepository.save(mainOrder);

		return getTableNotPayOrder(tableId);
	}

	public ApiResponse<TableOrderResponse> getTableNotPayOrder(Integer tableId) {
		MainOrderEntity mainOrder = mainOrderRepository.getMainOrder(tableId);

		if (mainOrder == null) {
			return ApiResponse.error(ResultCode.MAIN_ORDER_NOT_EXIST);
		}

		// 建立桌號訂單總資料 builder
		TableOrderResponse.TableOrderResponseBuilder builder = TableOrderResponse.builder()
				.code(mainOrder.getCode())
				.tableId(mainOrder.getTableId())
				.totalPrice(mainOrder.getTotalPrice())
				.paymentStatus(mainOrder.getPaymentStatus());

		// 取得訂單
		List<OrderEntity> orderList = orderRepository.findByMainOrderCodeAndIsActive(mainOrder.getCode(), true);

		// 建立訂單和訂單細項回應
		List<OrderResponse> orderListRes = orderList.stream().map(order -> {
			List<OrderItemPreviewResponse> orderItems = orderItemRepository.findTop3ByOrderId(order.getId()).stream()
					.map(orderItem -> {
						OrderItemPreviewResponse orderItemRes = OrderItemPreviewResponse.builder()
								.menuName(orderItem.getMenuName())
								.quantity(orderItem.getQuantity())
								.build();
						return orderItemRes;
					}).collect(Collectors.toList());

			OrderResponse orderRes = OrderResponse.builder()
					.id(order.getId())
					.orderStatus(order.getOrderStatus())
					.totalPrice(order.getTotalPrice())
					.orderItems(orderItems)
					.build();
			return orderRes;
		}).collect(Collectors.toList());

		builder.orders(orderListRes);

		TableOrderResponse tableOrderRes = builder.build();

		return ApiResponse.success(tableOrderRes);
	}
}
