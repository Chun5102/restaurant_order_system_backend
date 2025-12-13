package com.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.course.entity.MainOrderEntity;
import com.course.model.dto.TableStatusDto;

public interface MainOrderRepository extends JpaRepository<MainOrderEntity, Long> {

    MainOrderEntity findByCode(String code);

    @Query("SELECT M FROM MainOrderEntity M WHERE M.tableId = ?1 AND M.isActive = true AND M.paymentStatus = '未付款'")
    MainOrderEntity getMainOrder(Integer tableId);

    @Query("SELECT new com.course.model.dto.TableStatusDto( " +
            " CASE WHEN COUNT(DISTINCT M)>0 THEN true ELSE false END, " +
            " COUNT(O)) " +
            " FROM MainOrderEntity M JOIN OrderEntity O ON M.code = O.mainOrderCode " +
            " WHERE M.tableId = :tableId AND M.isActive = true AND M.paymentStatus = '未付款'")
    TableStatusDto getTableStatus(@Param("tableId") Integer id);
}
