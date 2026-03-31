package com.example.restaurantmanagement.service;

public interface ReportExportService {
    byte[] exportToExcel(int year);
    byte[] exportToPdf(int year);
}