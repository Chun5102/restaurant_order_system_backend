package com.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.course.entity.TableEntity;

import jakarta.persistence.LockModeType;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

    TableEntity findByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    TableEntity findByIdAndStatus(Integer id, String status);

}
