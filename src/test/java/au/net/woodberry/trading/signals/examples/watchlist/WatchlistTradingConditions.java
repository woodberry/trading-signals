package au.net.woodberry.trading.signals.examples.watchlist;

import au.net.woodberry.trading.signals.conditions.TradingConditions;
import au.net.woodberry.trading.signals.model.impl.Stock;
import org.joda.time.DateTime;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

public class WatchlistTradingConditions implements TradingConditions<Stock> {
    
    private DateTime lastTimestamp;
    
    @Override
    public AnonymousCondition<Stock> shouldShortList() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {

                // Rule: Short list (re-enter into the watchlist) if a stock contains a new price entry after the last recorded timestamp
                boolean isSatisfied = false;
                if (stock.hasPrices() && (lastTimestamp == null || stock.getLatestPrice().getTimestamp().isAfter(lastTimestamp))) {
                    lastTimestamp = stock.getLatestPrice().getTimestamp();
                    isSatisfied = true;
                }
                return isSatisfied;
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldHold() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnonymousCondition<Stock> shouldEnter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnonymousCondition<Stock> shouldExit() {
        throw new UnsupportedOperationException();
    }

    private DateTime getLastTimestamp() {
        return lastTimestamp;
    }

    private void setLastTimestamp(DateTime lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
}
