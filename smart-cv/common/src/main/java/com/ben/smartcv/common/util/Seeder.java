package com.ben.smartcv.common.util;

import com.ben.smartcv.common.contract.command.JobCommand;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class Seeder {

    public static List<JobCommand.CreateJob> extractJobDescriptions(String fileName) {
        List<JobCommand.CreateJob> jobDescriptions = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("fake-jobs/" + fileName);
        try {
            InputStream inputStream = resource.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                JobCommand.CreateJob job = JobCommand.CreateJob.builder()
                        .id(UUID.randomUUID().toString())
                        .organizationName(getCellValue(row, 0))
                        .position(getCellValue(row, 1))
                        .fromSalary(Double.parseDouble(getCellValue(row, 2)))
                        .toSalary(Double.parseDouble(getCellValue(row, 3)))
                        .expiredAt(TimeHelper.generateRandomInstant())
                        .requirements(getCellValue(row, 4))
                        .build();
                jobDescriptions.add(job);
            }

            workbook.close();
            inputStream.close();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read Excel file: " + fileName, e);
        }
        return jobDescriptions;
    }

    private static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

}
