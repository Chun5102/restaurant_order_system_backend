package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.course.entity.MorderEntity;
import com.course.model.vo.MorderVo;

@Repository
public interface MorderRepository extends JpaRepository<MorderEntity, Long> {

	Boolean existsByCode(String code);

	MorderEntity findByCode(String code);

	void deleteByCode(String code);

	@Query("SELECT new com.course.model.MorderVo(m.code, m.morderStatus,m.totalPrice) FROM MorderEntity m WHERE m.tableNumber = ?1 AND m.paymentStatus = ?2")
	List<MorderVo> findByTableNumAndPayStatus(Integer tableNumber, Short paymentStatus);
}
