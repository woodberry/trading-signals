package au.net.woodberry.trading.signals.helpers;

import au.net.woodberry.trading.signals.model.Price;
import au.net.woodberry.trading.signals.model.Stock;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DataFeed {

    private Map<Stock, List<Price>> dataFeed = new HashMap<>();

    /**
     * Seed the stock with price data. Should be called before invoking fetch method
     */
    public void seed(Stock stock, List<Price> prices) {
        checkNotNull(stock);
        checkNotNull(prices);
        checkArgument(!prices.isEmpty());
        if (!dataFeed.containsKey(stock)) {
            dataFeed.put(stock, prices);
        } else {
            stock.getPrices().addAll(prices);
        }
    }

    /**
     * Fetch a limited result; up to and including the date time specified
     */
    public Stock fetch(Stock stock, DateTime dateTime) {
        for (Stock key : dataFeed.keySet()) {
            if (key.equals(stock)) {
                for (Price price : dataFeed.get(key)) {
                    if (price.getTimestamp().equals(dateTime)) {
                        stock.getPrices().add(price);
                    }
                }
            }
        }
        return stock;
    }

    public Set<Stock> getFeed() {
        return dataFeed.keySet();
    }
}
