package org.tracker;

import org.tracker.excel.ExcelProcessor;
import org.tracker.report.ReportType;
import org.tracker.trackerui.TrackerUIApplication;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class Tracker {

    private static ExcelProcessor excelProcessor;

    public static void createQueue(List<File> files) {
        excelProcessor = new ExcelProcessor();

        files.forEach(file -> excelProcessor.addToQueue(file.getPath()));
    }

    public static void initialize(ReportType reportType) {
        if (reportType.equals(ReportType.FAF)) excelProcessor.processFafFiles();
        else if (reportType.equals(ReportType.PRESUMED)) excelProcessor.processPresumedFiles();
        else throw new RuntimeException("Error! Unknown report type.");
    }

    public static BigDecimal getArchivesValue() {
        return excelProcessor.getBigDecimal();
    }

    public static void main(String[] args) {
        TrackerUIApplication.main(args);
    }
}
