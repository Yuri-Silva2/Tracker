package org.tracker.report;

import org.tracker.processing.Processing;
import org.tracker.report.builder.FaFReportBuilder;
import org.tracker.report.builder.PresumedReportBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class Report {

    private String dateFromFile;

    abstract Processing processData();

    public BigDecimal finish(String filePath) {
        Processing processing = processData();

        processing.execute(filePath);
        processing.checkData();

        ArrayList<?>[] invoices = processing.invoiceList();
        return createReportFile(invoices);
    }

    private BigDecimal createReportFile(ArrayList<?>[] invoices) {
        if (invoices.length == 2) {
            PresumedReportBuilder presumedReportBuilder = new PresumedReportBuilder();
            presumedReportBuilder.build(invoices[0], invoices[1]);

            dateFromFile = presumedReportBuilder.getDate();

            return presumedReportBuilder.getFinalValue();

        } else if (invoices.length == 1) {
            FaFReportBuilder faFReportBuilder = new FaFReportBuilder();
            faFReportBuilder.build(invoices[0]);

            return faFReportBuilder.getFinalValue();
        }
        return null;
    }

    public String getDateFromFile() {
        return dateFromFile;
    }
}
