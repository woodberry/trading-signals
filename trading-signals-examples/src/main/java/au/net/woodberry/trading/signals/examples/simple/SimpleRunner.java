package au.net.woodberry.trading.signals.examples.simple;

import au.net.woodberry.trading.signals.helpers.DataFeed;
import au.net.woodberry.trading.signals.model.Price;
import au.net.woodberry.trading.signals.model.Stock;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Arrays;

/**
 * Demonstrates a simple example that goes through each trading day, receives a data feed for a number of stocks 
 * executes the various trading nop based on the simple trading conditions presented to it
 * 
 * @see SimpleTradingConditions
 *
 */
public class SimpleRunner {
    static final SimpleTradingConditions TRADING_CONDITIONS = new SimpleTradingConditions();
    static final SimpleController CONTROLLER = new SimpleController(TRADING_CONDITIONS);
    static final DataFeed DATA_FEED = new DataFeed();
    static final DateTime START = DateTime.parse("2012-01-01");
    static final DateTime END = DateTime.parse("2012-01-12");
    
    static {
        DATA_FEED.seed(new Stock("A", Days.ONE), Arrays.asList(
                new Price(DateTime.parse("2012-01-01"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-02"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-03"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-04"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-05"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-06"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-07"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-08"), 4, 4, 4, 4, 4, 4),
                new Price(DateTime.parse("2012-01-09"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-10"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-11"), 1, 1, 1, 1, 1, 1)));
        for (Stock mockStock : DATA_FEED.getFeed()) {
            CONTROLLER.add(mockStock);
        }
    }
    
    public static void main(String... args) {
        for (DateTime today = START; today.isBefore(END) || today.isEqual(END); today = today.plus(Days.ONE)) {
            for (Stock stock : CONTROLLER.getStocks()) {
                DATA_FEED.fetch(stock, today);
                CONTROLLER.execute(stock);
                System.out.println(today + ": Stock [" + stock.getSymbol() + "]" + " is in state [" + CONTROLLER.getCurrentState(stock) + "]");
            }
        }
    }
}
