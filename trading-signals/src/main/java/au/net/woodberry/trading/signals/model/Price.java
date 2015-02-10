package au.net.woodberry.trading.signals.model;

import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkNotNull;

public class Price {

    private double open;

    private double high;

    private double low;
    
    private double close;

    private double adjustedClose;
    
    private double volume;

    private DateTime timestamp;
    
    public Price(DateTime timestamp, double open, double high, double low, double close, double adjustedClose, double volume) {
        checkNotNull(timestamp);
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
    }
    
    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getAdjustedClose() {
        return adjustedClose;
    }

    public double getVolume() {
        return volume;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
