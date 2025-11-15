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

	@Query("SELECT new com.course.model.MorderItemVo(m.name, mi.quantity,mi.subtotal)" + " FROM MorderItemEntity mi"
			+ " JOIN MenuEntity m ON m.id = mi.menuId" + " WHERE mi.morderCode = ?1")
	List<MorderItemVo> findByMorderCodeToVo(String code);
}
