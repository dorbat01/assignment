package au.com.livewirelabs.assignment;

import static org.junit.Assert.assertNotNull;

import au.com.livewirelabs.assignment.guice.modules.StockExchangeModule;
import au.com.livewirelabs.assignment.service.StockExchangeService;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AssignmentUnitTest {

	@Test
	public void givenStockExchangeServiceInjectedInStockExchangeModule_ThenReturnValueIsNotNull() {
		Injector injector = Guice.createInjector(new StockExchangeModule());
		StockExchangeService guiceService = injector.getInstance(StockExchangeService.class);
		assertNotNull(guiceService);
	}

}
