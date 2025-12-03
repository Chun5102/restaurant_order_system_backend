package com.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.course.entity.MenuEntity;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

	@Query("SELECT M FROM MenuEntity M WHERE M.category = ?1 AND M.status = 2")
	Page<MenuEntity> getMenuByCategory(String category, Pageable pageable);

	boolean existsByName(String name);
}
