package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.course.entity.MorderItemEntity;
import com.course.model.vo.MorderItemVo;

@Repository
public interface MorderItemRepository extends JpaRepository<MorderItemEntity, Long> {

	List<MorderItemEntity> findByMorderCode(String code);

	@Query(value = "SELECT m.name AS name, " +
			"mi.quantity AS quantity, " +
			"mi.subtotal AS subtotal " +
			"FROM morder_item mi " +
			"JOIN menu m ON m.id = mi.menu_id " +
			"WHERE mi.morder_code = :code", nativeQuery = true)
	List<MorderItemVo> findByMorderCodeToVo(String code);
}
