package au.com.livewirelabs.assignment.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;

import au.com.livewirelabs.assignment.guice.dao.StockDao;
import au.com.livewirelabs.assignment.guice.exception.InsufficientUnitsException;
import au.com.livewirelabs.assignment.guice.exception.InvalidCodeException;
import au.com.livewirelabs.assignment.model.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RunWith(MockitoJUnitRunner.class)
public class StockExchangeServiceTest {

    private static final String EXPECTED_CODE_NAB = "NAB";
    private static final String EXPECTED_CODE_ASX = "ASX";

    @Mock
    private Logger mockLogger;

    @Mock
    private StockDao mockStockDao;

    @InjectMocks
    private StockExchangeServiceImpl stockExchangeService;

    @Captor
    private ArgumentCaptor<Stock> captorStock;

    @Test
    public void whenBuy_thenBuySuccessfulWithoutException() throws InsufficientUnitsException, InvalidCodeException {
        final Integer EXPECTED_VOLUME_ASX = 4;
        final Integer EXPECTED_TOTAL_VOLUME_ASX = 10;
        Stock expectedStock2 = new Stock(EXPECTED_CODE_ASX);
        expectedStock2.setTotalVolume(EXPECTED_TOTAL_VOLUME_ASX);
        Mockito.when(mockStockDao.getByCode(eq(EXPECTED_CODE_ASX))).thenReturn(expectedStock2);
        Mockito.doNothing().when(mockStockDao).save(any(Stock.class));
        stockExchangeService.buy(EXPECTED_CODE_ASX, EXPECTED_VOLUME_ASX);
        Mockito.verify(mockStockDao).getByCode(eq(EXPECTED_CODE_ASX));
        Mockito.verify(mockStockDao).save(captorStock.capture());
        assertThat(captorStock.getValue().getTotalVolume(), equalTo(6));
    }

    @Test
    public void whenGetOrderBookTotalVolume_thenCorrectVolumesOnMap() {
        final Integer EXPECTED_VOLUME_NAB = 2;
        final Integer EXPECTED_VOLUME_ASX = 5;
        List<Stock> expectedStocks = new ArrayList<>();
        Stock expectedStock1 = new Stock(EXPECTED_CODE_NAB);
        expectedStock1.setTotalCost(BigDecimal.TEN);
        expectedStock1.setTotalVolume(EXPECTED_VOLUME_NAB);
        expectedStocks.add(expectedStock1);
        Stock expectedStock2 = new Stock(EXPECTED_CODE_ASX);
        expectedStock2.setTotalCost(BigDecimal.ONE);
        expectedStock2.setTotalVolume(EXPECTED_VOLUME_ASX);
        expectedStocks.add(expectedStock2);
        Mockito.when(mockStockDao.getAllStocks()).thenReturn(expectedStocks);
        Map<String, Integer> actuals = stockExchangeService.getOrderBookTotalVolume();
        assertThat(actuals.get(EXPECTED_CODE_NAB), notNullValue());
        assertThat(actuals.get(EXPECTED_CODE_NAB), equalTo(EXPECTED_VOLUME_NAB));

        assertThat(actuals.get(EXPECTED_CODE_ASX), notNullValue());
        assertThat(actuals.get(EXPECTED_CODE_ASX), equalTo(EXPECTED_VOLUME_ASX));
        Mockito.verify(mockStockDao).getAllStocks();
    }

    @Test
    public void whenGetOrderBookTotalVolumeWithEmptyMap_thenEmptyMapReturn() {
        List<Stock> expectedStocks = new ArrayList<>();
        Mockito.when(mockStockDao.getAllStocks()).thenReturn(expectedStocks);
        Map<String, Integer> actuals = stockExchangeService.getOrderBookTotalVolume();
        assertThat(actuals, anEmptyMap());
        Mockito.verify(mockStockDao).getAllStocks();
    }

    @Test
    public void whenGetOrderBookTotalVolumeWithNull_thenEmptyMapReturn() {
        List<Stock> expectedStocks = new ArrayList<>();
        Mockito.when(mockStockDao.getAllStocks()).thenReturn(null);
        Map<String, Integer> actuals = stockExchangeService.getOrderBookTotalVolume();
        assertThat(actuals, anEmptyMap());
        Mockito.verify(mockStockDao).getAllStocks();
    }

    @Test
    public void whenGetTradingCosts_thenCorrectCostsSum() {
        final BigDecimal EXPECTED_COST_NAB = BigDecimal.TEN;
        final BigDecimal EXPECTED_COST_ASX = BigDecimal.ONE;
        List<Stock> expectedStocks = new ArrayList<>();
        Stock expectedStock1 = new Stock(EXPECTED_CODE_NAB);
        expectedStock1.setTotalCost(EXPECTED_COST_NAB);
        expectedStocks.add(expectedStock1);
        Stock expectedStock2 = new Stock(EXPECTED_CODE_ASX);
        expectedStock2.setTotalCost(EXPECTED_COST_ASX);
        expectedStocks.add(expectedStock2);
        Mockito.when(mockStockDao.getAllStocks()).thenReturn(expectedStocks);
        BigDecimal actual = stockExchangeService.getTradingCosts();
        assertThat(actual, equalTo(new BigDecimal("11").setScale(2)));
        Mockito.verify(mockStockDao).getAllStocks();
    }

    @Test
    public void whenGetTradingCostsWithNull_thenZeroBigDecimalValueReturn() {
        List<Stock> expectedStocks = new ArrayList<>();
        Mockito.when(mockStockDao.getAllStocks()).thenReturn(null);
        BigDecimal actual = stockExchangeService.getTradingCosts();
        assertTrue(actual.compareTo(BigDecimal.ZERO) == 0);
        Mockito.verify(mockStockDao).getAllStocks();
    }
}
