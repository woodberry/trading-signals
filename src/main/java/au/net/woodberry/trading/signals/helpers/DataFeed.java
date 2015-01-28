package au.net.woodberry.trading.signals.helpers;

import au.net.woodberry.trading.signals.model.Instrument;
import au.net.woodberry.trading.signals.model.TimeSeries;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple implementation of a generic data feed which contains both an instrument and its related time-series data
 *
 * @param <T> The instrument for the feed
 * @param <S> The time-series data
 */
@SuppressWarnings("unchecked")
public class DataFeed<T extends Instrument, S extends TimeSeries> {

    private Map<T, List<S>> dataFeed = new HashMap<>();

    /**
     * Seed the market data feed with some time series data. This should be invoked before fetch method
     */
    public void seed(T t, List<S> s) {
        checkNotNull(t);
        checkNotNull(s);
        checkArgument(!s.isEmpty());
        if (!dataFeed.containsKey(t)) {
            dataFeed.put(t, s);
        } else {
            t.getTimeSeries().add(s);
        }
    }

    /**
     * Fetch a limited result; up to and including the date time specified
     */
    public T fetch(T t, DateTime dateTime) {
        for (T key : dataFeed.keySet()) {
            if (key.equals(t)) {
                for (S s : dataFeed.get(key)) {
                    if (s.getTimestamp().equals(dateTime)) {
                        t.getTimeSeries().add(s);
                    }
                }
            }
        }
        return t;
    }

    public Set<T> getFeed() {
        return dataFeed.keySet();
    }
}
