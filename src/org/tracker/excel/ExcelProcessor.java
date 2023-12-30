package org.tracker.excel;

import org.tracker.model.Value;
import org.tracker.report.FaFReport;
import org.tracker.report.PresumedReport;
import org.tracker.report.Report;
import org.tracker.report.builder.ConferenceReportBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ExcelProcessor {

    private final ArrayList<Value> finalValues = new ArrayList<>();
    private BigDecimal bigDecimal = BigDecimal.ZERO;

    private final ExcelQueue queue;

    private Report report;

    public ExcelProcessor() {
        queue = new ExcelQueue();
    }

    public void addToQueue(File file) {
        queue.enqueue(file.getPath());
    }

    public void processPresumedFiles() {
        while (queue.isEmpty()) {
            String filePath = queue.dequeue();
            if (filePath != null) {
                report = new PresumedReport();
                BigDecimal finalValue = report.finish(filePath);
                bigDecimal = bigDecimal.add(finalValue);
                finalValues.add(new Value(report.getDateFromFile(), finalValue));
            }
        }

        new ConferenceReportBuilder().build(finalValues);
    }

    public void processFafFiles() {
        while (queue.isEmpty()) {
            String filePath = queue.dequeue();
            if (filePath != null) {
                report = new FaFReport();
                BigDecimal finalValue = report.finish(filePath);
                bigDecimal = bigDecimal.add(finalValue);
            }
        }
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }
}
