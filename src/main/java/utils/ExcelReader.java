package utils;

// Importing Apache POI classes to work with Excel files
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Importing Log4j for logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to read test data from an Excel file.
 * This supports a data-driven testing approach.
 */
public class ExcelReader {

    private static final Logger logger = LogManager.getLogger(ExcelReader.class); // Logger instance for this class

    /**
     * Reads data from a specific sheet in an Excel file and returns it as a list of maps.
     * Each map in the list represents a data row, with column headers as keys.
     * This structure is ideal for iterating through test data sets.
     *
     * @param filePath The path to the Excel file.
     * @param sheetName The name of the sheet to read from.
     * @return A list of maps, where each map is a row of test data.
     * @throws IOException If the file cannot be found or read.
     */
    public List<Map<String, String>> getData(String filePath, String sheetName) throws IOException {
        logger.info("Reading data from Excel file: " + filePath + ", Sheet: " + sheetName); // Log read start
        List<Map<String, String>> dataList = new ArrayList<>(); // List to hold all rows as maps

        FileInputStream fis = new FileInputStream(filePath); // Open the Excel file
        XSSFWorkbook workbook = new XSSFWorkbook(fis); // Create workbook instance
        XSSFSheet sheet = workbook.getSheet(sheetName); // Get specific sheet

        if (sheet == null) {
            logger.error("Sheet '" + sheetName + "' not found in the workbook."); // Log missing sheet
            workbook.close();
            fis.close();
            return dataList; // Return empty list
        }

        Row headerRow = sheet.getRow(0); // First row is header row
        if (headerRow == null) {
            logger.error("Header row is missing in the sheet: " + sheetName); // Log missing headers
            workbook.close();
            fis.close();
            return dataList;
        }

        DataFormatter formatter = new DataFormatter(); // Formatter to get cell values as strings

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from row 1 (skip header)
            Row row = sheet.getRow(i);
            if (row == null) {
                logger.warn("Skipping empty row at index: " + i); // Skip blank rows
                continue;
            }

            Map<String, String> rowMap = new HashMap<>(); // Map to hold one row's key-value data

            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                String key = headerRow.getCell(j).getStringCellValue(); // Get column header
                String value = formatter.formatCellValue(row.getCell(j)); // Get cell value as string
                rowMap.put(key, value); // Add key-value to map
                logger.debug("Row " + i + " - [" + key + " : " + value + "]"); // Log each entry for debugging
            }

            dataList.add(rowMap); // Add the row's map to the final list
        }

        logger.info("Successfully read " + dataList.size() + " row(s) of data from sheet: " + sheetName); // Log success

        workbook.close(); // Close workbook
        fis.close(); // Close file input stream

        return dataList; // Return complete list of row data
    }

}
