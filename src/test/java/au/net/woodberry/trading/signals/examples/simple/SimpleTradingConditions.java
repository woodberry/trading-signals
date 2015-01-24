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
                if (isValid(stock)) {
                    return stock.getPrices().get(latestPrice(stock)).getClose() >= 2; // Shortlist when close >= 2
                }
                return false;
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldEnter() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                if (isValid(stock)) {
                    return stock.getPrices().get(latestPrice(stock)).getClose() >= 3; // Enter when close >= 3
                }
                return false;
            }
        };
    }

    @Override
    public AnonymousCondition<Stock> shouldHold() {
        return new AnonymousCondition<Stock>() {
            @Override
            public boolean isSatisfied(Stock stock) {
                if (isValid(stock)) {
                    return stock.getPrices().get(latestPrice(stock)).getClose() >= 2; // Hold when close >= 2
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
                if (isValid(stock)) {
                    return stock.getPrices().get(latestPrice(stock)).getClose() < 2; // Exit when close < 2
                }
                return false;
            }
        };
    }

    private static boolean isValid(Stock stock) {
        return stock != null && stock.getPrices() != null && !stock.getPrices().isEmpty();
    }

    private static int latestPrice(Stock stock) {
        return stock.getPrices().size() - 1;
    }
}
