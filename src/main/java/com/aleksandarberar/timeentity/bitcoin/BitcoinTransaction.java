package com.aleksandarberar.timeentity.bitcoin;

import java.math.BigDecimal;

public class BitcoinTransaction {

    private final BitcoinAddress addressFrom;
    private final BitcoinAddress addressTo;
    private final BigDecimal amount;

    public BitcoinTransaction(BitcoinAddress addressFrom, BitcoinAddress addressTo, BigDecimal amount) {
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.amount = amount;
    }

    public BitcoinAddress getAddressFrom() {
        return addressFrom;
    }

    public BitcoinAddress getAddressTo() {
        return addressTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
