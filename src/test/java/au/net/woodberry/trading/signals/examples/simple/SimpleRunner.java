package au.net.woodberry.trading.signals.examples.simple;

import au.net.woodberry.trading.signals.model.Stock;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Demonstrates a simple example that goes through each trading day, receives a data feed for a number of stocks 
 * executes the various trading signals based on the simple trading conditions presented to it 
 * 
 * @see SimpleTradingConditions
 *
 */
public class SimpleRunner {
    
    static final SimpleTradingConditions TRADING_CONDITIONS = new SimpleTradingConditions();
    static final SimpleController CONTROLLER = new SimpleController(TRADING_CONDITIONS);
    static final DateTime START = DateTime.parse("2012-01-01");
    static final DateTime END = DateTime.parse("2012-01-09");
    
    static {
        for (Stock mockStock : SimpleSimulatedDataFeed.stocks()) {
            CONTROLLER.add(mockStock);
        }
    }
    
    public static void main(String... args) {
        for (DateTime today = START; today.isBefore(END) || today.isEqual(END); today = today.plus(Days.ONE)) {
            for (Stock stock : CONTROLLER.getStocks()) {
                CONTROLLER.execute(SimpleSimulatedDataFeed.fetch(stock, today));
                System.out.println(today + ": Stock [" + stock.getSymbol() + "]" + " is in state [" + CONTROLLER.getCurrentState(stock) + "]");
            }
        }
    }
}
