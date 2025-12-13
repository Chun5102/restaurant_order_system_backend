package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.course.entity.OrderItemEntity;
import com.course.model.dto.OrderItemDto;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

	List<OrderItemEntity> findByOrderId(Long orderId);

	@Query("SELECT new com.course.model.dto.OrderItemDto(oi.menuName, oi.quantity, oi.subtotal) " +
			" FROM MainOrderEntity mo " +
			" JOIN OrderEntity o ON mo.code = o.mainOrderCode " +
			" JOIN OrderItemEntity oi ON o.id = oi.orderId " +
			" WHERE mo.code = ?1")
	List<OrderItemDto> getAllOrderItems(String code);

	List<OrderItemEntity> findTop3ByOrderId(Long id);

}
