package com.course.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.TableEntity;
import com.course.enums.ResultCode;
import com.course.model.dto.TableStatusDto;
import com.course.model.request.TableRequest;
import com.course.model.response.ApiResponse;
import com.course.model.response.TableResponse;
import com.course.model.response.TableStatusResponse;
import com.course.repository.MainOrderRepository;
import com.course.repository.TableRepository;
import com.course.utils.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MainOrderRepository mainOrderRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public ApiResponse<Object> addTable() {

        TableEntity tableEntity = TableEntity.builder()
                .openedAt(null)
                .status("空閒")
                .code(UUID.randomUUID().toString())
                .build();

        tableRepository.save(tableEntity);
        return ApiResponse.success();
    }

    public ApiResponse<Object> updateTable(Integer id, TableRequest req) {
        TableEntity tableEntity = tableRepository.findById(id).orElse(null);
        if (tableEntity != null) {
            tableEntity.setStatus(req.getStatus());
            if (req.getIsCodeChange()) {
                tableEntity.setCode(UUID.randomUUID().toString());
            }
            tableRepository.save(tableEntity);
        } else {
            return ApiResponse.error(ResultCode.TABLE_NOT_EXIST);
        }
        return ApiResponse.success();
    }

    public ApiResponse<String> openTable(String code) {

        TableEntity tableEntity = tableRepository.findByCode(code);
        if (tableEntity == null) {
            return ApiResponse.error(ResultCode.TABLE_NOT_EXIST);
        }

        if ("空閒".equals(tableEntity.getStatus())) {
            LocalDateTime now = LocalDateTime.now();

            tableEntity.setStatus("使用中");
            tableEntity.setOpenedAt(now);
            tableRepository.save(tableEntity);
        }

        String token = jwtUtil.generateTableToken(tableEntity);
        return ApiResponse.success(token);
    }

    public ApiResponse<Object> checkTable(Integer id, LocalDateTime openedAt) {
        TableEntity tableEntity = tableRepository.findById(id).orElse(null);

        if (tableEntity == null) {
            return ApiResponse.error(ResultCode.TABLE_NOT_EXIST);
        }

        if (!tableEntity.getOpenedAt().isEqual(openedAt)) {
            return ApiResponse.error(ResultCode.NOT_SAME_GUEST);
        }

        return ApiResponse.success();

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
            return ApiResponse.error(ResultCode.TABLE_NOT_EXIST);
        }
    }

    public ApiResponse<TableStatusResponse> getTableStatus(Integer id) {
        TableEntity tableEntity = tableRepository.findById(id).orElse(null);
        if (tableEntity == null) {
            return ApiResponse.error(ResultCode.TABLE_NOT_EXIST);
        }

        TableStatusDto tableStatusDto = mainOrderRepository.getTableStatus(id);

        TableStatusResponse tableStatusResponse = TableStatusResponse.builder()
                .isOpened(tableStatusDto.isOpened())
                .orderCount(tableStatusDto.getOrderCount())
                .build();

        return ApiResponse.success(tableStatusResponse);
    }
}
