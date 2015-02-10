package au.net.woodberry.trading.signals.model;

import org.joda.time.ReadablePeriod;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Stock {

    private final String symbol;
    private final ReadablePeriod readablePeriod;
    private List<Price> prices;

    public Stock(String symbol, ReadablePeriod readablePeriod) {
        checkNotNull(symbol);
        checkNotNull(readablePeriod);
        this.symbol = symbol;
        this.readablePeriod = readablePeriod;
        this.prices = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public ReadablePeriod getReadablePeriod() {
        return readablePeriod;
    }

    public boolean hasPrices() {
        return prices != null && !prices.isEmpty();
    }

    public Price getLatestPrice() {
        if (hasPrices()) {
            int latest = getPrices().size() - 1;
            return getPrices().get(latest);
        }
        return null;
    }

    public ReadablePeriod getPricePeriod() {
        return getReadablePeriod();
    }
}
