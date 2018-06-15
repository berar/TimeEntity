package com.aleksandarberar.timeentity.bitcoin;

import com.aleksandarberar.timeentity.TimeEntity;
import com.aleksandarberar.timeentity.TimeEntityEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BitcoinMain {

    public static final BiFunction<TimeEntityEvent<List<BitcoinTransaction>>, BitcoinLedger, BitcoinLedger> FUNCTION = (i1, i2) -> {
        List<BitcoinTransaction> transactions = i1.getEvent();
        transactions.forEach(t -> {
            i2.map.putIfAbsent(t.getAddressFrom(), new BigDecimal("0.0"));
            i2.map.putIfAbsent(t.getAddressTo(), new BigDecimal("0.0"));
            i2.map.compute(t.getAddressFrom(), (a, v) -> v.subtract(t.getAmount()));
            i2.map.compute(t.getAddressTo(), (a, v) -> v.add(t.getAmount()));
        });
        return i2;
    };

    public static void main(String[] args) {
        BitcoinLedger initialState = new BitcoinLedger();
        TimeEntity<BitcoinLedger, List<BitcoinTransaction>> bitcoinLedgerTimeEntity
                = new TimeEntity<>(initialState, FUNCTION);
    }
}
