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
	@Column(name = "table_id", nullable = false)
	private Integer tableId;

	/**
	 * 訂單總價=>主訂單原始總價+加點訂單原始總價
	 * 主訂單專屬
	 */
	@Column(name = "total_price", nullable = true)
	private BigDecimal totalPrice;

	/**
	 * 訂單原始總價
	 */
	@Column(name = "base_price", nullable = false)
	private BigDecimal basePrice;

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

	/**
	 * 訂單是否刪除
	 */
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	/**
	 * 訂單是否為加點
	 */
	@Column(name = "is_add_on", nullable = false)
	private Boolean isAddOn;

	/**
	 * 訂單主訂單編號
	 * 訂單為加點才有值
	 */
	@Column(name = "original_morder_code", nullable = true)
	private String originalMorderCode;
}
