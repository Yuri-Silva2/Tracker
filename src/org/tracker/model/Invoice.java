package org.tracker.model;

import java.math.BigDecimal;

public class Invoice {
    private final String partner;
    private final String number;
    private final String branch;
    private final String type;
    private final String issuanceDate;
    private final String entryDate;
    private final String model;
    private final String series;
    private final int cfop;
    private BigDecimal productAmount;
    private final int productNumber;
    private final String productID;
    private final String cstICMS;
    private final int taxRate;
    private final BigDecimal icms;
    private double percentage;
    private BigDecimal presumedValue;

    public Invoice(
            String partner,
            String number,
            String branch,
            String type,
            String issuanceDate,
            String entryDate,
            String model,
            String series,
            int cfop,
            BigDecimal productAmount,
            int productNumber,
            String productID,
            String cstICMS,
            int taxRate,
            BigDecimal icms,
            double percentage
    ) {
        this.partner = partner;
        this.number = number;
        this.branch = branch;
        this.type = type;
        this.issuanceDate = issuanceDate;
        this.entryDate = entryDate;
        this.model = model;
        this.series = series;
        this.cfop = cfop;
        this.productAmount = productAmount;
        this.productNumber = productNumber;
        this.productID = productID;
        this.cstICMS = cstICMS;
        this.taxRate = taxRate;
        this.icms = icms;
        this.percentage = percentage;
    }

    public String partner() {
        return partner;
    }

    public String number() {
        return number;
    }

    public String branch() {
        return branch;
    }

    public String type() {
        return type;
    }

    public String issuanceDate() {
        return issuanceDate;
    }

    public String entryDate() {
        return entryDate;
    }

    public String model() {
        return model;
    }

    public String series() {
        return series;
    }

    public int cfop() {
        return cfop;
    }

    public void updateProductAmount(BigDecimal value) {
        this.productAmount = value;
    }

    public BigDecimal productAmount() {
        return productAmount;
    }

    public int productNumber() {
        return productNumber;
    }

    public String productID() {
        return productID;
    }

    public String cstICMS() {
        return cstICMS;
    }

    public int taxRate() {
        return taxRate;
    }

    public BigDecimal icms() {
        return icms;
    }

    public double percentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getPresumedValue() {
        return presumedValue;
    }

    public void setPresumedValue(BigDecimal value) {
        this.presumedValue = value;
    }
}
