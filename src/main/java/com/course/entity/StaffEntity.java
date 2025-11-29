package com.course.entity;

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
@Table(name = "staff")
public class StaffEntity extends BaseEntity {

	/**
	 * 員工編號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 員工名字
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * 員工帳號
	 */
	@Column(name = "username", nullable = false)
	private String username;

	/**
	 * 員工密碼
	 */
	@Column(name = "password", nullable = false)
	private String password;

	/**
	 * 員工角色
	 */
	@Column(name = "role", nullable = false)
	private String role;
}
