package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	Boolean existsByMainOrderCodeAndIsActive(String mainOrderCode, Boolean isActive);

	List<OrderEntity> findByMainOrderCodeAndIsActive(String code, Boolean isActive);

}
