package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.course.entity.MenuEntity;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

	@Query("SELECT M FROM MenuEntity M WHERE M.status = 2")
	List<MenuEntity> findOnSaleMenu();
}
