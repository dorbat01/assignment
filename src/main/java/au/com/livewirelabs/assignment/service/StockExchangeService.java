package au.com.livewirelabs.assignment.service;

import au.com.livewirelabs.assignment.guice.exception.InsufficientUnitsException;
import au.com.livewirelabs.assignment.guice.exception.InvalidCodeException;

import java.math.BigDecimal;
import java.util.Map;

public interface StockExchangeService {

    /**
     * Buy stock
     */
    void buy(String code, Integer units) throws InsufficientUnitsException, InvalidCodeException;

    /**
     * Sell stock
     */
    void sell(String code, Integer units) throws InvalidCodeException;

    /**
     * Report aggregate volume available for each code
     */
    Map<String, Integer> getOrderBookTotalVolume();

    /**
     * Returns dollar value of trading activity
     */
    BigDecimal getTradingCosts();
}
