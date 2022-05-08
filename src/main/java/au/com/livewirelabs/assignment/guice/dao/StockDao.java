package au.com.livewirelabs.assignment.guice.dao;

import au.com.livewirelabs.assignment.model.Stock;

import java.util.List;

public interface StockDao {

    void save(Stock stock);

    Stock getByCode(String stockCode);

    List<Stock> getAllStocks();
}
