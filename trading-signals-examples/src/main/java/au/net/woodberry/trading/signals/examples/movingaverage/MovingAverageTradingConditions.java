package au.net.woodberry.trading.signals.examples.movingaverage;

import au.net.woodberry.trading.signals.conditions.TradingConditions;
import au.net.woodberry.trading.signals.model.Price;
import au.net.woodberry.trading.signals.model.Stock;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TADecimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class MovingAverageTradingConditions implements TradingConditions<Stock> {

    private final int shortTerm;
    private final int longTerm;

    public MovingAverageTradingConditions(int shortTerm, int longTerm) {
        checkArgument(shortTerm < longTerm);
        this.shortTerm = shortTerm;
        this.longTerm = longTerm;
    }
    
    @Override
    public AnonymousCondition<Stock> shouldShortList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnonymousCondition<Stock> shouldNoLongerShortList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnonymousCondition<Stock> shouldHold() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AnonymousCondition<Stock> shouldComplete() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AnonymousCondition<Stock> shouldEnter() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                if (stock.hasPrices()) {
                    TimeSeries timeSeries = fromPrices(stock.getPrices());
                    Indicator<TADecimal> smaShortTerm = new SMAIndicator(new ClosePriceIndicator(timeSeries), shortTerm);
                    Indicator<TADecimal> smaLongTerm = new SMAIndicator(new ClosePriceIndicator(timeSeries), longTerm);
                    int last = stock.getPrices().size() - 1;
                    return smaShortTerm.getValue(last).isGreaterThan(smaLongTerm.getValue(last));
                }
                return false;
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldExit() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                if (stock.hasPrices()) {
                    TimeSeries timeSeries = fromPrices(stock.getPrices());
                    Indicator<TADecimal> smaShortTerm = new SMAIndicator(new ClosePriceIndicator(timeSeries), shortTerm);
                    Indicator<TADecimal> smaLongTerm = new SMAIndicator(new ClosePriceIndicator(timeSeries), longTerm);
                    int last = stock.getPrices().size() - 1;
                    return smaShortTerm.getValue(last).isLessThan(smaLongTerm.getValue(last));
                }
                return false;
            }
        };
    }

    private static TimeSeries fromPrices(List<Price> prices) {
        List<Tick> ticks = new ArrayList<>();
        for (Price price : prices) {
            double adjustFactor = (price.getClose() - price.getAdjustedClose()) / price.getClose();
            double open = price.getOpen() - (price.getOpen() * adjustFactor);
            double high = price.getHigh() - (price.getHigh() * adjustFactor);
            double low = price.getLow() - (price.getLow() * adjustFactor);
            Tick tick = new Tick(price.getTimestamp(), open, high, low, price.getAdjustedClose(), price.getVolume());
            ticks.add(tick);
        }
        return new TimeSeries(null, ticks);
    }
}
