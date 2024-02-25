package org.tracker.processing;

import org.tracker.excel.ExcelDataReader;
import org.tracker.model.Invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FaFProcessing implements Processing {

    private final List<Invoice> invoices = new ArrayList<>();

    @Override
    public void execute(String filePath) {
        ExcelDataReader excelDataReader = new ExcelDataReader(filePath);

        invoices.addAll(excelDataReader.execute());

        for (Invoice invoice : this.invoices) {

            Optional<Invoice> optionalInvoice = invoices.stream()
                    .filter(invoice1 -> Objects.equals(invoice1.partner(), invoice.partner()) &&
                            Objects.equals(invoice1.number(), invoice.number()))
                    .findAny();

            if (optionalInvoice.isPresent()) {

                BigDecimal updatedAmount = optionalInvoice.get().productAmount().
                        add(invoice.productAmount()).setScale(2, RoundingMode.HALF_EVEN);

                optionalInvoice.get().updateProductAmount(updatedAmount);
            }
        }
    }

    @Override
    public void checkData() {
        invoices.removeIf(invoice -> !Objects.equals(invoice.branch(), "1000") &&
                !Objects.equals(invoice.cfop(), 1151));
    }

    @Override
    public ArrayList<?>[] invoiceList() {
        return new ArrayList<?>[]{(ArrayList<Invoice>) this.invoices};
    }
}
