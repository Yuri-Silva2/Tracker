package org.tracker.report.builder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.tracker.TrackerUtils;
import org.tracker.model.Invoice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PresumedReportBuilder implements ReportBuilder {

    private final String PATH = System.getProperty("user.home") + "/Downloads/";
    private String date = "";

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

        for (Object object : list[1]) {
            Invoice invoice = (Invoice) object;

            String partner = getPartnerInfo(invoice);
            String uf = invoice.branch().equals("1209") ? "BR|ES" : "BR|RS";

            BigDecimal decimalValue = invoice.getPresumedValue();
            finalValue = finalValue.add(decimalValue);

            if (date.isEmpty()) date = invoice.entryDate();

            if (invoice.type().equalsIgnoreCase("Entrada")) {
                String code = invoice.branch().equals("1209") ? "ES40000400" : "RS99980003";

                writeBodyInvoice(invoice,
                        code, partner, uf, decimalValue);

            } else if (invoice.type().equalsIgnoreCase("Saída")) {
                String code = invoice.branch().equals("1209") ? "ES10000129" : "RS99980001";

                writeBodyInvoice(invoice,
                        code, partner, uf, decimalValue);
            }
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

    private void writeBodyInvoice(Invoice invoice, String code, String partner, String uf,
                                  BigDecimal decimalValue) {
        try {
            printer.printRecord(
                    "OBSDET", "1000", invoice.branch(), partner,
                    "AMAI99", uf, code, "189", invoice.productNumber(),
                    invoice.productID(), invoice.icms(), invoice.percentage(), decimalValue, "0"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeInvoicesInFile(ArrayList<?>... list) {
        try {
            String PRESUMED_FILE_PREFIX = "Presumed";
            String fileName = TrackerUtils.createFileWithNumber(PRESUMED_FILE_PREFIX, PATH);

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

    public String getDate() {
        return date;
    }
}
