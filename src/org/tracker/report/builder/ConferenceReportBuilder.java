package org.tracker.report.builder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.tracker.model.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConferenceReportBuilder implements ReportBuilder {

    private final String PATH = System.getProperty("user.home") + "/Downloads/";

    private CSVPrinter printer;

    @Override
    public void build(ArrayList<?>... list) {
        writeValuesInFile(list);
    }

    private void writerValues(ArrayList<?>... list) {
        for (Object object : list[0]) {
            Value valueObject = (Value) object;
            writeValueInLine(valueObject);
        }
    }

    private void writeValueInLine(Value value) {
        try {
            String valueWithNewSeparator = String.valueOf(value.value())
                    .replace(".", ",");

            printer.printRecord(value.date(), valueWithNewSeparator);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeValuesInFile(ArrayList<?>... list) {
        try {
            String fileName = createFileWithNumber();

            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setDelimiter(';')
                    .setQuote('"')
                    .setRecordSeparator("\r\n")
                    .setIgnoreEmptyLines(true)
                    .setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL)
                    .build();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(PATH + fileName));
            printer = new CSVPrinter(writer, csvFormat);

            writerValues(list);
            printer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createFileWithNumber() {
        String fileNameWithPrefix = "Relatorio por dia" + ".csv";

        File file = new File(PATH + fileNameWithPrefix);

        if (file.exists()) {
            File[] files = new File(PATH).listFiles((dir, name) ->
                    name.matches("^" + "Relatorio por dia" + " \\(" + "(\\d+)\\" + ")" + ".csv$"));
            int maxNumber = 0;

            if (files != null) {

                for (File f : files) {
                    Matcher matcher = Pattern.compile("^" + "Relatorio por dia" + " \\(" + "(\\d+)\\" + ")" + ".csv$")
                            .matcher(f.getName());

                    if (matcher.find()) {
                        int number = Integer.parseInt(matcher.group(1));
                        maxNumber = Math.max(maxNumber, number);
                    }
                }
            }
            return "Relatorio por dia" + " (" + (maxNumber + 1) + ").csv";
        } else {
            return fileNameWithPrefix;
        }
    }
}
