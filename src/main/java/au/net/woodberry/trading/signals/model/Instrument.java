package au.net.woodberry.trading.signals.model;

import org.joda.time.ReadablePeriod;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Instrument<T> {

    private final String symbol;
    private final ReadablePeriod readablePeriod;
    private List<T> timeSeries;

    public Instrument(String symbol, ReadablePeriod readablePeriod) {
        checkNotNull(symbol);
        checkNotNull(readablePeriod);
        this.symbol = symbol;
        this.readablePeriod = readablePeriod;
        this.timeSeries = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }
    
    public List<T> getTimeSeries() {
        return timeSeries;
    }
    
    public boolean hasTimeSeries() {
        return timeSeries != null && !timeSeries.isEmpty();
    }
    
    public T getLatestTimeSeries() {
        if (hasTimeSeries()) {
            int latest = getTimeSeries().size() - 1;
            return getTimeSeries().get(latest);
        }
        return null;
    }
    
    public ReadablePeriod getReadablePeriod() {
        return readablePeriod;
    }
}
