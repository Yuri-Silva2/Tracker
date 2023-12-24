package org.tracker.processing;

import org.tracker.excel.ExcelDataReader;
import org.tracker.model.Invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PresumedProcessing implements Processing {

    private final List<Invoice> headerInvoices = new ArrayList<>();
    private final List<Invoice> bodyInvoices = new ArrayList<>();

    @Override
    public void execute(String filePath) {
        ExcelDataReader excelDataReader = new ExcelDataReader(filePath);
        List<Invoice> invoices = excelDataReader.execute();

        bodyInvoices.addAll(invoices);

        for (Invoice invoice : invoices) {
            Optional<Invoice> optionalInvoice = headerInvoices.stream().filter(frontInvoice -> Objects.
                    equals(frontInvoice.partner(), invoice.partner()) &&
                    Objects.equals(frontInvoice.number(), invoice.number())).findAny();

            if (optionalInvoice.isEmpty()) {
                headerInvoices.add(invoice);
            }

            calculatePresumedValue(invoice);
        }
    }

    @Override
    public void checkData() {
        removeInvoicesByBranch(headerInvoices, "1000", "1102", "1209");
        removeInvoicesByBranch(bodyInvoices, "1000", "1102", "1209");

        removeInvoicesByCfop(headerInvoices, 1201, 2201, 2208, 5101, 6101, 6107, 6108, 2202);
        removeInvoicesByCfop(bodyInvoices, 1201, 2201, 2208, 5101, 6101, 6107, 6108, 2202);

        removeInvoicesByCstIcms(headerInvoices);
        removeInvoicesByCstIcms(bodyInvoices);

        removeInvoicesByBranchAndCfop(headerInvoices);
        removeInvoicesByBranchAndCfop(bodyInvoices);
    }

    @Override
    public ArrayList<?>[] invoiceList() {
        return new ArrayList<?>[]{(ArrayList<?>) this.headerInvoices,
                (ArrayList<?>) this.bodyInvoices};
    }

    private void calculatePresumedValue(Invoice invoice) {
        invoice.setPercentage(66.67);

        BigDecimal presumedValue = invoice.icms().multiply(new BigDecimal("0.6667"))
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal presumedValueFromICMS = invoice.icms().subtract(invoice.productAmount().
                        multiply(new BigDecimal("0.04")))
                .setScale(2, RoundingMode.HALF_EVEN);

        int taxRate = invoice.taxRate();
        String cstICMS = invoice.cstICMS();
        int cfop = invoice.cfop();

        if (taxRate == 12) {
            if (Objects.equals(invoice.branch(), "1209") &&
                    Objects.equals(cstICMS, "000")) {

                invoice.setPercentage(90.83);
                presumedValue = invoice.icms().multiply(new BigDecimal("0.9083"))
                        .setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (taxRate == 17 && !Objects.equals(cfop, 6108) && !Objects.equals(cfop, 2202)) {
            if (Objects.equals(cstICMS, "000") && (!(presumedValue.subtract(presumedValueFromICMS).abs()
                    .compareTo(new BigDecimal("0.06")) <= 0))) {

                invoice.setPercentage(76.47);
                presumedValue = invoice.icms().multiply(new BigDecimal("0.7647"))
                        .setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (taxRate == 7 && Objects.equals(cstICMS, "000") &&
                !Objects.equals(cfop, 6108) && !Objects.equals(cfop, 2202)) {

            invoice.setPercentage(42.86);
            presumedValue = invoice.icms().multiply(new BigDecimal("0.4286"))
                    .setScale(2, RoundingMode.HALF_EVEN);
        }

        invoice.setPresumedValue(presumedValue);
    }

    private void removeInvoicesByBranch(List<Invoice> invoices, String... branches) {
        invoices.removeIf(invoice -> !Arrays.asList(branches).contains(invoice.branch()));
    }

    private void removeInvoicesByCfop(List<Invoice> invoices, Integer... cfops) {
        invoices.removeIf(invoice -> !Arrays.asList(cfops).contains(invoice.cfop()));
    }

    private void removeInvoicesByCstIcms(List<Invoice> invoices) {
        invoices.removeIf(invoice -> !Objects.equals(invoice.cstICMS(), "000") &&
                !Objects.equals(invoice.cstICMS(), "051"));
    }

    private void removeInvoicesByBranchAndCfop(List<Invoice> invoices) {
        invoices.removeIf(invoice -> Objects.equals(invoice.branch(), "1000") &&
                Objects.equals(invoice.cfop(), 6108));
    }
}
