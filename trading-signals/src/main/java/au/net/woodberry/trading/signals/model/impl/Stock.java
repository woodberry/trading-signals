package au.net.woodberry.trading.signals.model.impl;

import au.net.woodberry.trading.signals.model.Instrument;
import org.joda.time.ReadablePeriod;

import java.util.List;

public final class Stock extends Instrument<Price> {

    public Stock(String symbol, ReadablePeriod pricePeriod) {
        super(symbol, pricePeriod);
    }
    
    /**
     * Syntactic sugar for getTimeSeries()
     */
    public List<Price> getPrices() {
        return getTimeSeries();
    }

    /**
     * Syntactic sugar for hasTimeSeries()
     */
    public boolean hasPrices() {
        return hasTimeSeries();
    }

    /**
     * Syntactic sugar for getLatestTimeSeries
     */
    public Price getLatestPrice() {
        return getLatestTimeSeries();
    }

    /**
     * Syntactic sugar for getPricePeriod()
     */
    public ReadablePeriod getPricePeriod() {
        return getReadablePeriod();
    }
}
