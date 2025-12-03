package com.course.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.TableEntity;
import com.course.model.request.TableRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.TableResponse;
import com.course.repository.MorderRepository;
import com.course.repository.TableRepository;

import jakarta.transaction.Transactional;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MorderRepository morderRepository;

    @Transactional
    public ApiResponse addTable() {

        TableEntity tableEntity = TableEntity.builder()
                .openedAt(null)
                .status("空閒")
                .code(UUID.randomUUID().toString())
                .build();

        tableRepository.save(tableEntity);
        return ApiResponse.success();
    }

    public ApiResponse updateTable(Integer id, TableRequest req) {
        TableEntity tableEntity = tableRepository.findById(id).orElse(null);
        if (tableEntity != null) {
            tableEntity.setStatus(req.getStatus());
            if (req.getIsCodeChange()) {
                tableEntity.setCode(UUID.randomUUID().toString());
            }
            tableRepository.save(tableEntity);
        } else {
            return ApiResponse.error("401", "無此桌子");
        }
        return ApiResponse.success();
    }

    public ApiResponse<TableResponse> openTable(String code) {

        TableEntity tableEntity = tableRepository.findByCode(code);
        if (tableEntity == null) {
            return ApiResponse.error("401", "無此桌子");
        }

        boolean hasPendingOrder = morderRepository.existsByTableIdAndPaymentStatus(tableEntity.getId(), "已付款");
        if (hasPendingOrder) {
            return ApiResponse.error("403", "該桌已有未付款訂單，請用原本手機完成點餐");
        }

        tableEntity.setStatus("使用中");
        tableEntity.setOpenedAt(LocalDateTime.now());
        tableRepository.save(tableEntity);

        TableResponse tableResponse = TableResponse.builder()
                .id(tableEntity.getId())
                .openedAt(tableEntity.getOpenedAt())
                .qrCode(tableEntity.getCode())
                .build();
        return ApiResponse.success(tableResponse);
    }

    public ApiResponse<TableResponse> getTable(String code) {
        TableEntity tableEntity = tableRepository.findByCode(code);
        if (tableEntity != null) {
            TableResponse tableResponse = TableResponse.builder()
                    .id(tableEntity.getId())
                    .openedAt(tableEntity.getOpenedAt())
                    .qrCode(tableEntity.getCode())
                    .build();
            return ApiResponse.success(tableResponse);
        } else {
            return ApiResponse.error("401", "無此桌子");
        }
    }
}
