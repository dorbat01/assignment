package au.com.livewirelabs.assignment;

import au.com.livewirelabs.assignment.guice.exception.InsufficientUnitsException;
import au.com.livewirelabs.assignment.guice.exception.InvalidCodeException;
import au.com.livewirelabs.assignment.guice.modules.StockExchangeModule;
import au.com.livewirelabs.assignment.service.StockExchangeService;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Assignment {

    @Inject
    private Logger logger;
    @Inject
    private StockExchangeService stockExchangeService;

    public void trade(String stockCode) throws InvalidCodeException {
        int numberOfOrders = ThreadLocalRandom.current().nextInt(1, 100);
        try {
            for (int counter = 1; counter <= numberOfOrders; counter++) {
                try {
                    int numberOfUnits = ThreadLocalRandom.current().nextInt(1, 1000);
                    if (counter % 2 == 0 ) {
                        Logger.getAnonymousLogger().log(Level.INFO,
                                String.format("Buy %s:: units=%d",
                                        stockCode,
                                        numberOfUnits));
                        stockExchangeService.buy(stockCode, numberOfUnits);
                    } else {
                        Logger.getAnonymousLogger().log(Level.INFO,
                                String.format("Sell %s:: units=%d",
                                        stockCode,
                                        numberOfUnits));
                        stockExchangeService.sell(stockCode, numberOfUnits);
                    }
                } catch (InsufficientUnitsException e) {
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
                }
            }
        } catch (InvalidCodeException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new StockExchangeModule());
        Assignment assignment = injector.getInstance(Assignment.class);
        try {
            if (isValid(args)) {
                String stockCode = args[1];
                assignment.trade(stockCode);
            } else {
                System.out.println("Invalid argument! The expected argument format: -exchange {stock code}");
            }
        } catch (InvalidCodeException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

    private static boolean isValid(String[] args) {
        if (args.length == 2 && "-exchange".equalsIgnoreCase(args[0].trim())) {
            return true;
        }
        return false;
    }
}
