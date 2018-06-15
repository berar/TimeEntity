package com.aleksandarberar.timeentity.bitcoin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BitcoinLedger {

    protected final Map<BitcoinAddress, BigDecimal> map;

    public BitcoinLedger() {
        this.map = new HashMap<>();
    }
}
