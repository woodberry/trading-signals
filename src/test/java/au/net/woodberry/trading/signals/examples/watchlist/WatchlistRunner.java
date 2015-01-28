package au.net.woodberry.trading.signals.examples.watchlist;

import au.net.woodberry.trading.signals.helpers.DataFeed;
import au.net.woodberry.trading.signals.model.impl.Price;
import au.net.woodberry.trading.signals.model.impl.Stock;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Arrays;

public class WatchlistRunner {

    static final DateTime START = DateTime.parse("2012-01-01");
    static final DateTime END = DateTime.parse("2012-12-31");
    static final WatchlistTradingConditions TRADING_CONDITIONS = new WatchlistTradingConditions();
    static final WatchlistController CONTROLLER = new WatchlistController(TRADING_CONDITIONS, START, Days.ONE);
    static final DataFeed<Stock, Price> DATA_FEED = new DataFeed();

    static {
        DATA_FEED.seed(new Stock("A", Days.ONE), Arrays.asList(
                new Price(DateTime.parse("2012-01-05"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-10"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-15"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-20"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-25"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-30"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-02-05"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-02-10"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-02-15"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-02-20"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-02-25"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-03-05"), 1, 1, 1, 1, 1, 1)
        ));
        
        for (Stock stock : DATA_FEED.getFeed()) {
            CONTROLLER.add(stock);
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
