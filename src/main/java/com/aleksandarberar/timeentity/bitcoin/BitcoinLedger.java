package com.aleksandarberar.timeentity.bitcoin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BitcoinLedger {

    protected final Map<BitcoinAddress, BigDecimal> map;

    public BitcoinLedger() {
        this.map = new HashMap<>();
    }

    public BitcoinLedger(Map<BitcoinAddress, BigDecimal> map) {
        this.map = map;
    }

    public BigDecimal getBalance(String address) {
        return this.getBalance(new BitcoinAddress(address));
    }

    public BigDecimal getBalance(BitcoinAddress bitcoinAddress) {
        return this.map.get(bitcoinAddress);
    }
}
