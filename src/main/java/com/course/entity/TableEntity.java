package com.course.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables")
public class TableEntity extends BaseEntity {
    /**
     * 桌子號碼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 桌子狀態
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 桌子開啟時間
     */
    @Column(name = "opened_at", nullable = true)
    private LocalDateTime openedAt;

    /**
     * 桌子編號
     */
    @Column(name = "code", nullable = false)
    private String code;
}
