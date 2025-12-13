package com.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.course.service.ReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/report")
@Tag(name = "報表", description = "報表相關 API")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/orderCheckout")
    public void getReport(@RequestParam("type") String reportType, HttpServletResponse response)
            throws Exception {
        reportService.getReport(reportType, response);
    }
}
