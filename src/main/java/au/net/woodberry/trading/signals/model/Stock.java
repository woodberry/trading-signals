package au.net.woodberry.trading.signals.model;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private final String symbol;
    
    private List<Price> prices;
    
    public Stock(String symbol) {
        this.symbol = symbol;
        this.prices = new ArrayList<>(); 
    }
    
    public List<Price> getPrices() {
        return prices;
    }
    
    public void addPrice(Price price) {
        prices.add(price);
    }

    public String getSymbol() {
        return symbol;
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

}
