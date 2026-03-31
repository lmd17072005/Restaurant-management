package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.response.BestSellerResponse;
import com.example.restaurantmanagement.dto.response.CategoryReportResponse;
import com.example.restaurantmanagement.dto.response.MonthlyReportResponse;
import com.example.restaurantmanagement.dto.response.ReportSummaryResponse;
import com.example.restaurantmanagement.service.ReportExportService;
import com.example.restaurantmanagement.service.ReportService;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExportServiceImpl implements ReportExportService {

    private final ReportService reportService;

    @Override
    public byte[] exportToExcel(int year) {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);

            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0"));

            Sheet summarySheet = workbook.createSheet("Summary");
            ReportSummaryResponse summary = reportService.getSummary(year);

            String[][] summaryData = {
                    { "Report Year", String.valueOf(year) },
                    { "Total Revenue (VND)", String.valueOf(summary.getTotalRevenue()) },
                    { "Avg Order Value (VND)", String.valueOf(summary.getAvgOrderValue()) },
                    { "Total Invoices Paid", String.valueOf(summary.getTotalOrders()) }
            };

            for (int i = 0; i < summaryData.length; i++) {
                Row row = summarySheet.createRow(i);
                Cell labelCell = row.createCell(0);
                labelCell.setCellValue(summaryData[i][0]);
                labelCell.setCellStyle(headerStyle);
                row.createCell(1).setCellValue(summaryData[i][1]);
            }
            summarySheet.setColumnWidth(0, 6000);
            summarySheet.setColumnWidth(1, 4000);

            Sheet monthlySheet = workbook.createSheet("Monthly Report");
            List<MonthlyReportResponse> monthly = reportService.getMonthlyReport(year);

            String[] monthlyHeaders = { "Month", "Revenue (VND)", "Orders" };
            Row monthlyHeaderRow = monthlySheet.createRow(0);
            for (int i = 0; i < monthlyHeaders.length; i++) {
                Cell cell = monthlyHeaderRow.createCell(i);
                cell.setCellValue(monthlyHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            for (int i = 0; i < monthly.size(); i++) {
                Row row = monthlySheet.createRow(i + 1);
                MonthlyReportResponse m = monthly.get(i);
                row.createCell(0).setCellValue(m.getMonth());
                Cell revenueCell = row.createCell(1);
                revenueCell.setCellValue(m.getRevenue().doubleValue());
                revenueCell.setCellStyle(currencyStyle);
                row.createCell(2).setCellValue(m.getOrders());
            }
            for (int i = 0; i < 3; i++) monthlySheet.autoSizeColumn(i);

            Sheet bestSellerSheet = workbook.createSheet("Best Sellers");
            List<BestSellerResponse> bestSellers = reportService.getBestSellers(year, 10);

            String[] bsHeaders = { "Rank", "Dish", "Sales (qty)", "Revenue (VND)", "Contribution (%)" };
            Row bsHeaderRow = bestSellerSheet.createRow(0);
            for (int i = 0; i < bsHeaders.length; i++) {
                Cell cell = bsHeaderRow.createCell(i);
                cell.setCellValue(bsHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            for (int i = 0; i < bestSellers.size(); i++) {
                Row row = bestSellerSheet.createRow(i + 1);
                BestSellerResponse bs = bestSellers.get(i);
                row.createCell(0).setCellValue(bs.getRank());
                row.createCell(1).setCellValue(bs.getName());
                row.createCell(2).setCellValue(bs.getSales());
                Cell revenueCell = row.createCell(3);
                revenueCell.setCellValue(bs.getRevenue().doubleValue());
                revenueCell.setCellStyle(currencyStyle);
                row.createCell(4).setCellValue(bs.getPercentage());
            }
            for (int i = 0; i < 5; i++) bestSellerSheet.autoSizeColumn(i);

            Sheet categorySheet = workbook.createSheet("By Category");
            List<CategoryReportResponse> categories = reportService.getCategoryReport(year);

            String[] catHeaders = { "Category", "Sales (qty)", "Revenue (VND)", "Share (%)" };
            Row catHeaderRow = categorySheet.createRow(0);
            for (int i = 0; i < catHeaders.length; i++) {
                Cell cell = catHeaderRow.createCell(i);
                cell.setCellValue(catHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            for (int i = 0; i < categories.size(); i++) {
                Row row = categorySheet.createRow(i + 1);
                CategoryReportResponse c = categories.get(i);
                row.createCell(0).setCellValue(c.getCategoryName());
                row.createCell(1).setCellValue(c.getSales());
                Cell revenueCell = row.createCell(2);
                revenueCell.setCellValue(c.getRevenue().doubleValue());
                revenueCell.setCellStyle(currencyStyle);
                row.createCell(3).setCellValue(c.getPercentage());
            }
            for (int i = 0; i < 4; i++) categorySheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel", e);
        }
    }

    @Override
    public byte[] exportToPdf(int year) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont   = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Font headerFont  = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            Font normalFont  = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font accentFont  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0xFF, 0x5F, 0x2E));
            BaseColor headerBg = new BaseColor(0xFF, 0x5F, 0x2E);

            Paragraph title = new Paragraph("Restaurant Report " + year, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            ReportSummaryResponse summary = reportService.getSummary(year);
            document.add(new Paragraph("Summary", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD)));
            document.add(new Paragraph(" "));

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(60);
            summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            summaryTable.setSpacingAfter(20);

            addPdfRow(summaryTable, "Total Revenue", formatVnd(summary.getTotalRevenue()), normalFont, accentFont);
            addPdfRow(summaryTable, "Avg Order Value", formatVnd(summary.getAvgOrderValue()), normalFont, accentFont);
            addPdfRow(summaryTable, "Total Invoices Paid", String.valueOf(summary.getTotalOrders()), normalFont, accentFont);
            document.add(summaryTable);

            document.add(new Paragraph("Best-Selling Dishes", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD)));
            document.add(new Paragraph(" "));

            List<BestSellerResponse> bestSellers = reportService.getBestSellers(year, 10);
            PdfPTable bsTable = new PdfPTable(5);
            bsTable.setWidthPercentage(100);
            bsTable.setWidths(new float[]{ 1f, 4f, 2f, 3f, 2f });
            bsTable.setSpacingAfter(20);

            for (String h : new String[]{ "Rank", "Dish", "Sales", "Revenue", "Share" }) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setPadding(6);
                bsTable.addCell(cell);
            }
            for (BestSellerResponse bs : bestSellers) {
                bsTable.addCell(new PdfPCell(new Phrase("#" + bs.getRank(), normalFont)));
                bsTable.addCell(new PdfPCell(new Phrase(bs.getName(), normalFont)));
                bsTable.addCell(new PdfPCell(new Phrase(bs.getSales() + " sold", normalFont)));
                PdfPCell revCell = new PdfPCell(new Phrase(formatVnd(bs.getRevenue()), accentFont));
                bsTable.addCell(revCell);
                bsTable.addCell(new PdfPCell(new Phrase(bs.getPercentage() + "%", normalFont)));
            }
            document.add(bsTable);

            document.add(new Paragraph("Sales by Category", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD)));
            document.add(new Paragraph(" "));

            List<CategoryReportResponse> categories = reportService.getCategoryReport(year);
            PdfPTable catTable = new PdfPTable(4);
            catTable.setWidthPercentage(100);
            catTable.setWidths(new float[]{ 3f, 2f, 3f, 2f });

            for (String h : new String[]{ "Category", "Sales", "Revenue", "Share" }) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setPadding(6);
                catTable.addCell(cell);
            }
            for (CategoryReportResponse c : categories) {
                catTable.addCell(new PdfPCell(new Phrase(c.getCategoryName(), normalFont)));
                catTable.addCell(new PdfPCell(new Phrase(c.getSales() + " sold", normalFont)));
                PdfPCell revCell = new PdfPCell(new Phrase(formatVnd(c.getRevenue()), accentFont));
                catTable.addCell(revCell);
                catTable.addCell(new PdfPCell(new Phrase(c.getPercentage() + "%", normalFont)));
            }
            document.add(catTable);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to export PDF", e);
        }
    }

    private void addPdfRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        table.addCell(new PdfPCell(new Phrase(label, labelFont)));
        table.addCell(new PdfPCell(new Phrase(value, valueFont)));
    }

    private String formatVnd(Object amount) {
        if (amount == null) return "0 ₫";
        long val = ((java.math.BigDecimal) amount).longValue();
        return String.format("%,d ₫", val);
    }
}