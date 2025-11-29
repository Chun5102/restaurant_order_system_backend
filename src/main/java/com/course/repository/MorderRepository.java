package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.course.entity.MorderEntity;

@Repository
public interface MorderRepository extends JpaRepository<MorderEntity, Long> {

	Boolean existsByCode(String code);

	MorderEntity findByCode(String code);

	void deleteByCode(String code);

	@Query("SELECT M FROM MorderEntity M WHERE M.tableNumber = ?1 AND M.paymentStatus = '0'")
	List<MorderEntity> getTableNumNotPay(Integer tableNumber);
}
