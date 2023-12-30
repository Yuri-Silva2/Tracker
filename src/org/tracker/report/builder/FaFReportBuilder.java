package org.tracker.report.builder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.tracker.TrackerUtils;
import org.tracker.model.Invoice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FaFReportBuilder implements ReportBuilder {

    private final String PATH = System.getProperty("user.home") + "/Downloads/";

    private BigDecimal finalValue = BigDecimal.ZERO;

    private CSVPrinter printer;

    @Override
    public void build(ArrayList<?>... list) {
        writeInvoicesInFile(list);
    }

    private void writeInvoices(ArrayList<?>... list) {
        for (Object object : list[0]) {
            Invoice invoice = (Invoice) object;
            String partner = getPartnerInfo(invoice);
            writeInvoiceHeader(invoice, partner);
        }

        for (Object object : list[0]) {
            Invoice invoice = (Invoice) object;
            String partner = getPartnerInfo(invoice);
            finalValue = finalValue.add(invoice.productAmount());
            writeInvoiceBody(invoice, partner);
        }
    }

    private void writeInvoiceHeader(Invoice invoice, String partner) {
        try {
            printer.printRecord(
                    "OBS", "1000", invoice.branch(),
                    partner, "AMAI99", "AM - APURACAO INCENTIVADA"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeInvoiceBody(Invoice invoice, String partner) {
        try {
            printer.printRecord(
                    "OBSDET", "1000", invoice.branch(), partner,
                    "AMAI99", "BR|RS", "RS99013007", invoice.cfop(), "", "", "0",
                    "0", "0", invoice.productAmount()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeInvoicesInFile(ArrayList<?>... list) {
        try {
            String FAF_FILE_PREFIX = "Presumed";
            String fileName = TrackerUtils.createFileWithNumber(FAF_FILE_PREFIX, PATH);

            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setDelimiter(';')
                    .setQuote('"')
                    .setRecordSeparator("\r\n")
                    .setIgnoreEmptyLines(true)
                    .setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL)
                    .build();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(PATH + fileName));
            printer = new CSVPrinter(writer, csvFormat);

            writeInvoices(list);
            printer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPartnerInfo(Invoice invoice) {
        String type = "";

        if (invoice.type().equalsIgnoreCase("Entrada")) type = "0";
        else type = "1";

        return invoice.partner() + "|" + type + "|" + invoice.model() + "|" +
                invoice.series() + "|" + invoice.number() + "|" + invoice.issuanceDate();
    }

    public BigDecimal getFinalValue() {
        return finalValue;
    }
}
