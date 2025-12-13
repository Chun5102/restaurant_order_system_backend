package com.course.entity;

import java.math.BigDecimal;
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
@Table(name = "main_orders")
public class MainOrderEntity extends BaseEntity {
    /**
     * 主訂單編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 主訂單自訂編號
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * 主訂單桌號
     */
    @Column(name = "table_id", nullable = false)
    private Integer tableId;

    /**
     * 主訂單總價
     */
    @Column(name = "total_price", nullable = true)
    private BigDecimal totalPrice;

    /**
     * 主訂單狀態
     */
    @Column(name = "main_order_status", nullable = false)
    private String mainOrderStatus;

    /**
     * 主訂單付款狀態
     */
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    /**
     * 主訂單付款方式
     */
    @Column(name = "payment_method", nullable = true)
    private String paymentMethod;

    /**
     * 主訂單實際付款金額
     */
    @Column(name = "paid_amount", nullable = true)
    private BigDecimal paidAmount;

    /**
     * 主訂單找零
     */
    @Column(name = "change_amount", nullable = true)
    private BigDecimal changeAmount;

    /**
     * 主訂單付款時間
     */
    @Column(name = "paid_at", nullable = true)
    private LocalDateTime paidAt;

    /**
     * 主訂單是否刪除
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
