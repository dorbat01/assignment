package au.com.livewirelabs.assignment.service;

import au.com.livewirelabs.assignment.guice.constant.StockCode;
import au.com.livewirelabs.assignment.guice.dao.StockDao;
import au.com.livewirelabs.assignment.guice.exception.InsufficientUnitsException;
import au.com.livewirelabs.assignment.guice.exception.InvalidCodeException;
import au.com.livewirelabs.assignment.model.Stock;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StockExchangeServiceImpl implements StockExchangeService {

    @Inject
    private Logger logger;
    @Inject
    private StockDao stockDao;


    @Override
    public void buy(String code, Integer units) throws InsufficientUnitsException, InvalidCodeException {
        try {
            StockCode stockCode = StockCode.forName(code);
            Stock trade = stockDao.getByCode(stockCode.getCode());
            if (trade.getTotalVolume() >= units) {
                Integer deductUnit = units * -1;
                trade.calculateTotalVolume(deductUnit);
                trade.addTotalCost(stockCode.getRate());
                stockDao.save(trade);
                printBalance(trade.getCode(), trade.getTotalVolume(), trade.getTotalCostStr());
            } else {
                throw new InsufficientUnitsException(String.format("Insufficient units! The stock balance of %s is %d", code, trade.getTotalVolume()));
            }
        } catch (InvalidCodeException e) {
            throw e;
        } catch (NoResultException e) {
            throw new InsufficientUnitsException(String.format("Insufficient units! The stock balance of %s is zero", code));
        }
    }

    @Override
    public void sell(String code, Integer units) throws InvalidCodeException {
        StockCode stockCode = null;
        try {
            stockCode = StockCode.forName(code);
            Stock trade = stockDao.getByCode(stockCode.getCode());
            trade.calculateTotalVolume(units);
            trade.addTotalCost(stockCode.getRate());
            stockDao.save(trade);
            printBalance(trade.getCode(), trade.getTotalVolume(), trade.getTotalCostStr());
        } catch (InvalidCodeException e) {
            throw e;
        } catch (NoResultException e) {
            Stock trade = new Stock(stockCode.getCode());
            trade.setTotalVolume(units);
            trade.setTotalCost(stockCode.getRate());
            stockDao.save(trade);
            printBalance(trade.getCode(), trade.getTotalVolume(), trade.getTotalCostStr());
        }
    }

    @Override
    public Map<String, Integer> getOrderBookTotalVolume() {
        List<Stock> stocks = stockDao.getAllStocks();
        Map<String, Integer> map = stocks.stream()
                .collect(Collectors.toMap(stock -> stock.getCode(), stock -> stock.getTotalVolume()));
        return map;
    }

    @Override
    public BigDecimal getTradingCosts() {
        List<Stock> stocks = stockDao.getAllStocks();
        return stocks.stream().map((Stock stock) -> stock.getTotalCost()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void printBalance(String stockCode, Integer units, String totalCosts) {
        Logger.getAnonymousLogger().log(Level.INFO,
                String.format("%s Remaining volume %d and the income of the exchange is %s",
                        stockCode,
                        units,
                        totalCosts));
    }
}
