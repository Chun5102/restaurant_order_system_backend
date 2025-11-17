package com.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.entity.MorderItemEntity;

@Repository
public interface MorderItemRepository extends JpaRepository<MorderItemEntity, Long> {

	List<MorderItemEntity> findByMorderCode(String code);

}
