package org.tracker.excel;

import com.aspose.cells.Cell;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import org.tracker.model.Invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelDataReader {

    private final String FILE_PATH;

    public ExcelDataReader(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    public List<Invoice> execute() {
        try {
            Workbook workbook = new Workbook(FILE_PATH);
            Worksheet worksheet = workbook.getWorksheets().get(0);

            int maximumLines = worksheet.getCells().getMaxDataRow() + 1;

            return processDate(worksheet, maximumLines);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Invoice> processDate(Worksheet worksheet, int maximumLines) {
        List<Invoice> invoices = new ArrayList<>();

        for (int i = 4; i <= maximumLines; i++) {
            Row row = worksheet.getCells().checkRow(i);
            if (row == null) continue;

            if (isInvalidDocumentType(row)) continue;
            if (invoiceNotHaveNumber(row)) continue;

            Invoice invoice = createObject(row);
            invoices.add(invoice);
        }

        return invoices;
    }

    private Invoice createObject(Row row) {
        String partner = getStringValue(row, 14);
        String number = getStringValue(row, 10);
        String branch = getStringValue(row, 1);
        String type = getStringValue(row, 6);
        String date = getStringValue(row, 7);
        String entryDate = getStringValue(row, 8);
        String model = getStringValue(row, 9);
        String series = getStringValue(row, 11);
        int cfop = getIntValue(row, 21);
        BigDecimal productAmount = getDecimalValue(row, 29);
        int productNumber = getIntValue(row, 24);
        String productID = getStringValue(row, 25);
        String cstICMS = getStringValue(row, 35);
        int taxRate = getIntValue(row, 37);
        BigDecimal icms = getDecimalValue(row, 38);
        return new Invoice(partner, number, branch,
                type, date, entryDate, model, series, cfop, productAmount,
                productNumber, productID, cstICMS, taxRate, icms,
                00.00);
    }

    private BigDecimal getDecimalValue(Row row, int number) {
        return BigDecimal.valueOf(getColumn(row, number).getFloatValue())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private int getIntValue(Row row, int number) {
        return getColumn(row, number).getIntValue();
    }

    private String getStringValue(Row row, int number) {
        return getColumn(row, number).getStringValue();
    }

    private Cell getColumn(Row row, int number) {
        return row.getCellOrNull(number);
    }

    private boolean invoiceNotHaveNumber(Row row) {
        String number = getStringValue(row, 10);
        return number == null;
    }

    private boolean isInvalidDocumentType(Row row) {
        String documentType = getStringValue(row, 12);
        return !Objects.equals(documentType, "00 - Documento regular") &&
                !Objects.equals(documentType, "01 - Documento Regular Extemporâneo") &&
                !Objects.equals(documentType, "06 - Documento Fiscal Complementar") &&
                !Objects.equals(documentType, "07 - Documento Fiscal Complementar Extemporâneo") &&
                !Objects.equals(documentType, "08 - Documento Fiscal emitido com base em Regime Especial ou Norma Específica");
    }
}
