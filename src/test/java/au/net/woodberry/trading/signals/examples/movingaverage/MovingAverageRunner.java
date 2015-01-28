package au.net.woodberry.trading.signals.examples.movingaverage;

import au.net.woodberry.trading.signals.helpers.DataFeed;
import au.net.woodberry.trading.signals.model.impl.Price;
import au.net.woodberry.trading.signals.model.impl.Stock;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Arrays;

public class MovingAverageRunner {
    
    static final int SHORT_TERM = 3;
    static final int LONG_TERM = 10;
    static final MovingAverageTradingConditions TRADING_CONDITIONS = new MovingAverageTradingConditions(SHORT_TERM, LONG_TERM);
    static final MovingAverageController CONTROLLER = new MovingAverageController(TRADING_CONDITIONS);
    static final DataFeed<Stock, Price> DATA_FEED = new DataFeed();
    static final DateTime START = DateTime.parse("2014-06-20");
    static final DateTime END = DateTime.parse("2014-08-06");
    
    static {
        DATA_FEED.seed(new Stock("A", Days.ONE), Arrays.asList(
                new Price(DateTime.parse("2014-06-20"), 33.45, 33.5, 33.13, 33.13, 32.17, 4339600),
                new Price(DateTime.parse("2014-06-23"), 33.34, 33.49, 33.28, 33.34, 32.37, 3539600),
                new Price(DateTime.parse("2014-06-24"), 33.19, 33.23, 33.02, 33.08, 32.12, 3594100),
                new Price(DateTime.parse("2014-06-25"), 32.99, 33.04, 32.8, 32.8, 31.85, 5449400),
                new Price(DateTime.parse("2014-06-26"), 32.94, 33.24, 32.85, 33.24, 32.28, 4928600),
                new Price(DateTime.parse("2014-06-27"), 33.2, 33.32, 33.02, 33.03, 32.07, 13990200),
                new Price(DateTime.parse("2014-06-30"), 33, 33.02, 32.78, 32.78, 31.83, 5713500),
                new Price(DateTime.parse("2014-07-01"), 32.81, 32.87, 32.24, 32.3, 31.36, 7521000),
                new Price(DateTime.parse("2014-07-02"), 32.63, 33.18, 32.6, 33.18, 32.22, 6477700),
                new Price(DateTime.parse("2014-07-03"), 33.29, 33.39, 33.16, 33.31, 32.35, 3737400),
                new Price(DateTime.parse("2014-07-04"), 33.5, 33.85, 33.43, 33.73, 32.75, 4391100),
                new Price(DateTime.parse("2014-07-07"), 33.75, 33.85, 33.62, 33.73, 32.75, 2644900),
                new Price(DateTime.parse("2014-07-08"), 33.71, 33.71, 33.45, 33.59, 32.62, 3669300),
                new Price(DateTime.parse("2014-07-09"), 33.41, 33.45, 33.16, 33.27, 32.31, 3558100),
                new Price(DateTime.parse("2014-07-10"), 33.36, 33.49, 33.25, 33.39, 32.42, 3607600),
                new Price(DateTime.parse("2014-07-11"), 33.33, 33.72, 33.21, 33.67, 32.69, 5255700),
                new Price(DateTime.parse("2014-07-14"), 33.84, 33.94, 33.72, 33.84, 32.86, 2948100),
                new Price(DateTime.parse("2014-07-15"), 33.86, 34.07, 33.74, 34.01, 33.02, 4478100),
                new Price(DateTime.parse("2014-07-16"), 34.04, 34.05, 33.77, 33.98, 33, 3669100),
                new Price(DateTime.parse("2014-07-17"), 34.16, 34.18, 34, 34, 33.02, 4539200),
                new Price(DateTime.parse("2014-07-18"), 33.71, 34.18, 33.71, 34.18, 33.19, 4535800),
                new Price(DateTime.parse("2014-07-21"), 34.29, 34.37, 34.19, 34.25, 33.26, 3351300),
                new Price(DateTime.parse("2014-07-22"), 34.34, 34.34, 34.11, 34.2, 33.21, 3339800),
                new Price(DateTime.parse("2014-07-23"), 34.31, 34.55, 34.28, 34.41, 33.41, 4938600),
                new Price(DateTime.parse("2014-07-24"), 34.52, 34.55, 34.4, 34.49, 33.49, 3338700),
                new Price(DateTime.parse("2014-07-25"), 34.54, 34.6, 34.44, 34.59, 33.59, 6428300),
                new Price(DateTime.parse("2014-07-28"), 34.56, 34.78, 34.46, 34.69, 33.69, 3461000),
                new Price(DateTime.parse("2014-07-29"), 34.68, 35.11, 34.62, 35.11, 34.09, 4175500),
                new Price(DateTime.parse("2014-07-30"), 35.06, 35.27, 34.92, 35.18, 34.16, 3811700),
                new Price(DateTime.parse("2014-07-31"), 35.25, 35.4, 35.2, 35.32, 34.3, 4047200),
                new Price(DateTime.parse("2014-08-01"), 35, 35.04, 34.74, 34.93, 33.92, 5164500),
                new Price(DateTime.parse("2014-08-04"), 34.78, 34.84, 34.66, 34.77, 33.76, 2027000),
                new Price(DateTime.parse("2014-08-05"), 34.88, 34.88, 34.56, 34.69, 33.69, 3615600),
                new Price(DateTime.parse("2014-08-06"), 34.55, 34.63, 34.4, 34.63, 33.63, 3286200)
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
