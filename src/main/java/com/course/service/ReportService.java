package com.course.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.entity.MainOrderEntity;
import com.course.model.dto.OrderItemDto;
import com.course.repository.MainOrderRepository;
import com.course.repository.OrderItemRepository;
import com.course.utils.JasperReportUtil;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportService {

    @Autowired
    private MainOrderRepository mainOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void getReport(String reportType, HttpServletResponse response) throws Exception {

        MainOrderEntity mainOrder = mainOrderRepository.getMainOrder(1);

        HashMap<String, Object> parameters = new HashMap<String, Object>();

        LocalDateTime createAt = mainOrder.getCreateAt();
        String receiptCreateAt = createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // LocalDateTime paidAt = mainOrder.getPaidAt();
        // String receiptpPidAt = paidAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd
        // HH:mm:ss"));

        parameters.put("tableId", mainOrder.getTableId());
        parameters.put("code", mainOrder.getCode());
        parameters.put("createdAt", receiptCreateAt);
        parameters.put("paidAt", mainOrder.getPaidAt());
        parameters.put("paymentMethod", mainOrder.getPaymentMethod());
        parameters.put("totalPrice", mainOrder.getTotalPrice());
        parameters.put("actualPaid", mainOrder.getPaidAmount());
        parameters.put("change", mainOrder.getChangeAmount());

        List<OrderItemDto> orderItems = orderItemRepository.getAllOrderItems(mainOrder.getCode());

        Map<String, OrderItemDto> mergedMap = new LinkedHashMap<>();

        // 合併同訂單細項
        for (OrderItemDto item : orderItems) {
            String key = item.getMenuName();
            if (mergedMap.containsKey(key)) {
                OrderItemDto existingValue = mergedMap.get(key);
                existingValue.setQuantity(existingValue.getQuantity() + item.getQuantity());
                existingValue.setSubtotal(existingValue.getSubtotal().add(item.getSubtotal()));
            } else {
                mergedMap.put(key, item);
            }
        }

        List<OrderItemDto> finalList = new ArrayList<>(mergedMap.values());

        String jasperPath = JasperReportUtil.getJasperFileDir("OrderCheckoutDetail");
        if (reportType.equals("pdf")) {
            JasperReportUtil.exportToPdf(jasperPath, parameters, finalList, response);
        } else if (reportType.equals("html")) {
            JasperReportUtil.exportToHtml(jasperPath, parameters, finalList, response);
        }
    }

}
