package au.net.woodberry.trading.signals.examples.simple;

import au.net.woodberry.trading.signals.impl.domain.Price;
import au.net.woodberry.trading.signals.impl.domain.Stock;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleSimulatedDataFeed {

    private static Map<Stock, List<Price>> simulatedDataFeed = new HashMap<>();

    static {
        simulatedDataFeed.put(new Stock("A"), Arrays.asList(
                new Price(DateTime.parse("2012-01-01"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-02"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-03"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-04"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-05"), 4, 4, 4, 4, 4, 4),
                new Price(DateTime.parse("2012-01-06"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-07"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-08"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-09"), 1, 1, 1, 1, 1, 1)));

        simulatedDataFeed.put(new Stock("B"), Arrays.asList(
                new Price(DateTime.parse("2012-01-01"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-02"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-03"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-04"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-05"), 4, 4, 4, 4, 4, 4),
                new Price(DateTime.parse("2012-01-06"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-07"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-08"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-09"), 1, 1, 1, 1, 1, 1)));

        simulatedDataFeed.put(new Stock("C"), Arrays.asList(
                new Price(DateTime.parse("2012-01-01"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-02"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-03"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-04"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-05"), 4, 4, 4, 4, 4, 4),
                new Price(DateTime.parse("2012-01-06"), 3, 3, 3, 3, 3, 3),
                new Price(DateTime.parse("2012-01-07"), 2, 2, 2, 2, 2, 2),
                new Price(DateTime.parse("2012-01-08"), 1, 1, 1, 1, 1, 1),
                new Price(DateTime.parse("2012-01-09"), 1, 1, 1, 1, 1, 1)));
    }

    public static Stock fetch(Stock stock, DateTime dateTime) {
        for (Stock key : simulatedDataFeed.keySet()) {
            if (key.getSymbol().equals(stock.getSymbol())) {
                for (Price price : simulatedDataFeed.get(key)) {
                    if (price.getTimestamp().equals(dateTime)) {
                        stock.getPrices().add(price);
                    }
                }
            }
        }
        return stock;
    }
    
    public static Set<Stock> stocks() {
        return simulatedDataFeed.keySet();
    }
}
