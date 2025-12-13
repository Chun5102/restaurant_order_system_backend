package com.course.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
	/**
	 * 訂單編號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 訂單總價
	 */
	@Column(name = "total_price", nullable = true)
	private BigDecimal totalPrice;

	/**
	 * 訂單狀態
	 */
	@Column(name = "order_status", nullable = false)
	private String orderStatus;

	/**
	 * 訂單是否刪除
	 */
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	/**
	 * 訂單主訂單編號
	 */
	@Column(name = "main_order_code", nullable = false)
	private String mainOrderCode;
}
