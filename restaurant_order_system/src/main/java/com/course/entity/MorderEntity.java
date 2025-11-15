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
@Table(name = "morder")
public class MorderEntity extends BaseEntity {
	/**
	 * 訂單編號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 訂單自訂編號
	 */
	@Column(name = "code", nullable = false, unique = true)
	private String code;

	/**
	 * 訂單桌號
	 */
	@Column(name = "table_number", nullable = false)
	private String tableNumber;

	/**
	 * 訂單總價
	 */
	@Column(name = "total_price", nullable = false)
	private BigDecimal totalPrice;

	/**
	 * 訂單狀態
	 */
	@Column(name = "morder_status", nullable = false)
	private String morderStatus;

	/**
	 * 訂單付款狀態
	 */
	@Column(name = "payment_status", nullable = false)
	private String paymentStatus;
}
