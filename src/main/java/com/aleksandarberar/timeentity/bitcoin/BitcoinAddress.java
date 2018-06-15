package com.aleksandarberar.timeentity.bitcoin;

public class BitcoinAddress {

    private final String addressValue;

    public BitcoinAddress(String addressValue) {
        this.addressValue = addressValue;
    }

    public String getAddressValue() {
        return addressValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitcoinAddress that = (BitcoinAddress) o;

        return addressValue != null ? addressValue.equals(that.addressValue) : that.addressValue == null;
    }

    @Override
    public int hashCode() {
        return addressValue != null ? addressValue.hashCode() : 0;
    }
}
