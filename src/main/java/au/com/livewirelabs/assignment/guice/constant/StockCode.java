package au.com.livewirelabs.assignment.guice.constant;

import au.com.livewirelabs.assignment.guice.exception.InvalidCodeException;

import java.math.BigDecimal;

public enum StockCode {

    NAB("NAB", BigDecimal.ZERO), CBA("CBA", BigDecimal.ZERO), QAN("QAN", BigDecimal.ZERO),
    CXA("CXA", new BigDecimal("0.5")), ASX("ASX", new BigDecimal("0.7"));

    private final String code;
    private final BigDecimal rate;

    StockCode(String code, BigDecimal rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() { return this.code; }
    public BigDecimal getRate() { return this.rate; }

    public static StockCode forName(String name) throws InvalidCodeException {
        try {
            StockCode result = StockCode.valueOf(name);
            return result;
        } catch (IllegalArgumentException e) {
            throw new InvalidCodeException(String.format("Invalid stock code: %s ", name));
        }
    }
}
