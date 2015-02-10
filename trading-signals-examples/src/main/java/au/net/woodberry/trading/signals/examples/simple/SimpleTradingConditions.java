package au.net.woodberry.trading.signals.examples.simple;

import au.net.woodberry.trading.signals.conditions.TradingConditions;
import au.net.woodberry.trading.signals.model.Stock;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

public class SimpleTradingConditions implements TradingConditions<Stock> {

    @Override
    public AnonymousCondition<Stock> shouldShortList() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                return stock.hasPrices() && stock.getLatestPrice().getClose() >= 2; // Shortlist when close >= 2
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldNoLongerShortList() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                return stock.hasPrices() && stock.getLatestPrice().getClose() <= 1; // // No short list when close <= 1
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldEnter() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                return stock.hasPrices() && stock.getLatestPrice().getClose() >= 3; // Enter when close >= 3
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldHold() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                return stock.hasPrices() && stock.getLatestPrice().getClose() >= 2; // Hold when close >= 2
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldExit() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                return stock.hasPrices() && stock.getLatestPrice().getClose() < 2; // Exit when close < 2
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldComplete() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                return true; // No extra or special logic required when a stock has completed trading
            }
        };
    }
}
