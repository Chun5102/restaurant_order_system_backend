package com.course.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name = "menu")
public class MenuEntity extends BaseEntity {
	/**
	 * 菜單編號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 菜單名稱
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * 菜單類別
	 */
	@Column(name = "category", nullable = false)
	private Integer category;

	/**
	 * 菜單價格
	 */
	@Column(name = "price", nullable = false)
	private BigDecimal price;

	/**
	 * 菜單描述
	 */
	@Column(name = "description", nullable = false)
	private String description;

	/**
	 * 菜單庫存
	 */
	@Column(name = "stock", nullable = false)
	private Integer stock;

	/**
	 * 菜單狀態
	 */
	@Column(name = "status", nullable = false)
	private Integer status;

	/**
	 * 菜單圖片資料
	 */
	@Lob
	@Column(name = "imageData", columnDefinition = "LONGBLOB")
	private byte[] imageData;

	/**
	 * 菜單圖片類型
	 */
	@Column(name = "imageType")
	private String imageType;
}
