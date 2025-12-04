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

	@Query("SELECT M FROM MorderEntity M WHERE M.tableId = ?1 AND M.isActive = true AND M.paymentStatus = '未付款'")
	List<MorderEntity> getTableNotPay(Integer tableId);

	boolean existsByTableIdAndPaymentStatus(Integer tableId, String paymentStatus);

	@Query("SELECT M FROM MorderEntity M WHERE M.tableId = ?1 AND M.isActive = true AND M.isAddOn = false AND M.paymentStatus = '未付款' AND M.originalMorderCode IS NULL")
	MorderEntity getMainMorder(Integer tableId);

}
