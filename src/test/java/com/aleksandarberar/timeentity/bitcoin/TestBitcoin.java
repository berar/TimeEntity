package com.aleksandarberar.timeentity.bitcoin;

import com.aleksandarberar.timeentity.TimeEntity;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestBitcoin {

    @Test
    public void testTransfer1BtcFromOneAddressToAnother() {

        Map<BitcoinAddress, BigDecimal> map = new HashMap<>();
        map.put(new BitcoinAddress("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH"), new BigDecimal("1.0"));
        map.put(new BitcoinAddress("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe"), new BigDecimal("0.0"));

        BitcoinLedger initialState = new BitcoinLedger(map);
        TimeEntity<BitcoinLedger, List<BitcoinTransaction>> bitcoinLedgerTimeEntity
                = new TimeEntity<>(initialState, BitcoinFunction.TRANSITION_FUNCTION);

        List<BitcoinTransaction> change = Arrays.asList(
                new BitcoinTransaction(
                        new BitcoinAddress("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH"),
                        new BitcoinAddress("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe"),
                        new BigDecimal("1.0")));
        bitcoinLedgerTimeEntity.apply(change);

        Assert.assertTrue(
                bitcoinLedgerTimeEntity.getFinalState().getBalance("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH").toPlainString().equals("0.0")
                && bitcoinLedgerTimeEntity.getFinalState().getBalance("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe").toPlainString().equals("1.0")
        );
    }

    @Test
    public void testMoreComplexTransfersExample() {

        Map<BitcoinAddress, BigDecimal> map = new HashMap<>();
        map.put(new BitcoinAddress("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH"), new BigDecimal("110.0"));
        map.put(new BitcoinAddress("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe"), new BigDecimal("150.0"));
        map.put(new BitcoinAddress("1GoC1B4GWdbnwzpCFNxqDb2zSL9UzQnwrW"), new BigDecimal("250.0"));

        BitcoinLedger initialState = new BitcoinLedger(map);
        TimeEntity<BitcoinLedger, List<BitcoinTransaction>> bitcoinLedgerTimeEntity
                = new TimeEntity<>(initialState, BitcoinFunction.TRANSITION_FUNCTION);

        List<BitcoinTransaction> change = Arrays.asList(
                new BitcoinTransaction(
                        new BitcoinAddress("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH"),
                        new BitcoinAddress("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe"),
                        new BigDecimal("1.0")));
        bitcoinLedgerTimeEntity.apply(change);

        List<BitcoinTransaction> change2nd = Arrays.asList(
                new BitcoinTransaction(
                        new BitcoinAddress("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe"),
                        new BitcoinAddress("1GoC1B4GWdbnwzpCFNxqDb2zSL9UzQnwrW"),
                        new BigDecimal("10.0")),
                
                new BitcoinTransaction(
                        new BitcoinAddress("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH"),
                        new BitcoinAddress("1GoC1B4GWdbnwzpCFNxqDb2zSL9UzQnwrW"),
                        new BigDecimal("10.0")));
        bitcoinLedgerTimeEntity.apply(change2nd);

        Assert.assertTrue(
                bitcoinLedgerTimeEntity.getFinalState().getBalance("1FfmbHfnpaZjKFvyi1okTjJJusN455paPH").toPlainString().equals("99.0")
                        && bitcoinLedgerTimeEntity.getFinalState().getBalance("14J5Q7ageKhM3miKd94DX44Kf6b7ko4BZe").toPlainString().equals("141.0")
                        && bitcoinLedgerTimeEntity.getFinalState().getBalance("1GoC1B4GWdbnwzpCFNxqDb2zSL9UzQnwrW").toPlainString().equals("270.0")
        );
    }
}
