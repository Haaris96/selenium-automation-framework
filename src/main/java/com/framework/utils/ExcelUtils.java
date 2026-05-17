package com.framework.utils;

import com.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel read/write utility using Apache POI.
 * Supports XLSX format only.
 */
public final class ExcelUtils {

    private static final Logger log = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {}

    // ─── Read ──────────────────────────────────────────────────────────────

    public static String getCellData(String sheetName, int rowNum, int colNum) {
        return getCellData(ConfigReader.getInstance().getExcelPath(), sheetName, rowNum, colNum);
    }

    public static String getCellData(String filePath, String sheetName, int rowNum, int colNum) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");

            Row row  = sheet.getRow(rowNum);
            if (row  == null) return "";
            Cell cell = row.getCell(colNum);
            if (cell == null) return "";

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell).trim();

        } catch (IOException e) {
            log.error("Error reading cell [{},{}] from sheet '{}': {}", rowNum, colNum, sheetName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static int getRowCount(String sheetName) {
        return getRowCount(ConfigReader.getInstance().getExcelPath(), sheetName);
    }

    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return 0;
            return sheet.getPhysicalNumberOfRows();
        } catch (IOException e) {
            log.error("Error getting row count for sheet '{}': {}", sheetName, e.getMessage());
            return 0;
        }
    }

    public static int getColumnCount(String sheetName, int rowNum) {
        try (FileInputStream fis = new FileInputStream(ConfigReader.getInstance().getExcelPath());
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return 0;
            Row row = sheet.getRow(rowNum);
            return row == null ? 0 : row.getPhysicalNumberOfCells();
        } catch (IOException e) {
            log.error("Error getting column count: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Returns all rows as a list of maps {columnHeader -> cellValue}.
     */
    public static List<Map<String, String>> getSheetData(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        String filePath = ConfigReader.getInstance().getExcelPath();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return data;

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return data;

            DataFormatter formatter = new DataFormatter();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) headers.add(formatter.formatCellValue(cell).trim());

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    rowData.put(headers.get(j), cell == null ? "" : formatter.formatCellValue(cell).trim());
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            log.error("Error reading sheet '{}': {}", sheetName, e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("Read {} rows from sheet '{}'", data.size(), sheetName);
        return data;
    }

    /**
     * Returns data as Object[][] suitable for TestNG @DataProvider.
     */
    public static Object[][] getDataProviderArray(String sheetName) {
        List<Map<String, String>> rows = getSheetData(sheetName);
        Object[][] result = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) result[i][0] = rows.get(i);
        return result;
    }

    // ─── Write ─────────────────────────────────────────────────────────────

    public static void setCellData(String sheetName, int rowNum, int colNum, String value) {
        String filePath = ConfigReader.getInstance().getExcelPath();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) sheet = workbook.createSheet(sheetName);

            Row row = sheet.getRow(rowNum);
            if (row == null) row = sheet.createRow(rowNum);

            row.createCell(colNum).setCellValue(value);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            log.info("Written '{}' to sheet='{}' row={} col={}", value, sheetName, rowNum, colNum);

        } catch (IOException e) {
            log.error("Error writing to Excel: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
