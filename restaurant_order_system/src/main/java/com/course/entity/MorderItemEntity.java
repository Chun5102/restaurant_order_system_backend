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
@Table(name = "morder_item")
public class MorderItemEntity {

	/**
	 * 訂單細項編號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 訂單自訂編號
	 */
	@Column(name = "morder_code", nullable = false)
	private String morderCode;

	/**
	 * 菜單編號
	 */
	@Column(name = "menu_id", nullable = false)
	private Long menuId;

	/**
	 * 菜單名字
	 */
	@Column(name = "menu_name", nullable = false)
	private String menuName;

	/**
	 * 餐點數量
	 */
	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	/**
	 * 訂單細項小計
	 */
	@Column(name = "subtotal", nullable = false)
	private BigDecimal subtotal;

	/**
	 * 訂單細項狀態
	 */
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
}
