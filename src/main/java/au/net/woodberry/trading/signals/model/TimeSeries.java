package au.net.woodberry.trading.signals.model;

import org.joda.time.DateTime;

public abstract class TimeSeries {

    protected DateTime timestamp;
    
    public DateTime getTimestamp() {
        return timestamp;
    }

}