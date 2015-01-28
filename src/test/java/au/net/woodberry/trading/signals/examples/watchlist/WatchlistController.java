package au.net.woodberry.trading.signals.examples.watchlist;

import au.net.woodberry.trading.signals.conditions.TradingConditions;
import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.model.impl.Stock;
import org.joda.time.DateTime;
import org.joda.time.ReadablePeriod;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.TransitionPriority;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static au.net.woodberry.trading.signals.examples.watchlist.WatchlistActions.IncrementPeriod;
import static au.net.woodberry.trading.signals.examples.watchlist.WatchlistActions.RemoveShortListedDate;
import static au.net.woodberry.trading.signals.examples.watchlist.WatchlistActions.SetShortListDate;
import static com.google.common.base.Preconditions.checkNotNull;

public class WatchlistController {

    private final StateMachineBuilder<WatchlistStateMachine, State, Event, Stock> builder;
    private final ReadablePeriod watchPeriod;
    private final DateTime startDate;
    private Map<Stock, WatchlistStateMachine> stateMachines;
    
    public WatchlistController(TradingConditions<Stock> conditions, DateTime startDate, ReadablePeriod watchPeriod) {
        this.builder = StateMachineBuilderFactory.create(WatchlistStateMachine.class, State.class, Event.class, Stock.class);
        this.stateMachines = new HashMap<>();
        this.watchPeriod = watchPeriod;
        this.startDate = startDate;
        builder.externalTransition().from(State.PASSIVE).to(State.WATCH).on(Event.SHORT_LISTED).when(conditions.shouldShortList());
        builder.externalTransition().from(State.WATCH).to(State.PASSIVE).on(Event.HAS_EXPIRED);
        builder.internalTransition(TransitionPriority.HIGH).within(State.PASSIVE).on(Event.PERIOD_ELAPSED).perform(new IncrementPeriod());
        builder.internalTransition(TransitionPriority.HIGH).within(State.WATCH).on(Event.PERIOD_ELAPSED).perform(new IncrementPeriod());
        builder.onEntry(State.WATCH).perform(new SetShortListDate());
        builder.onExit(State.WATCH).perform(new RemoveShortListedDate());
    }

    public void execute(Stock stock) {
        WatchlistStateMachine stateMachine = getStateMachine(stock);
        checkNotNull(stock, "Supplied input Stock is invalid: NULL");
        checkNotNull(stateMachine, "Could not execute trading signal for stock [" + stock + "]. Stock was not found");
        if (!stateMachine.isStarted()) {
            stateMachine.start();
            stateMachine.setCurrentDate(startDate);
        }
        switch (stateMachine.getCurrentState()) {
            case PASSIVE:
                stateMachine.fire(Event.SHORT_LISTED, stock);
                break;
            case WATCH:
                // Only necessitate firing of the has expired event when the current date is beyond the short listed date (S) plus duration of the watching period (d) i.e. S + d < T
                DateTime instant = stateMachine.getShortListedDate().plus(watchPeriod);
                if (instant.isBefore(stateMachine.getCurrentDate())) {
                    stateMachine.fire(Event.HAS_EXPIRED, stock);
                }
                break;
            default:
                stateMachine.terminate();
                throw new RuntimeException("Unexpected state [" + stateMachine.getCurrentState() + "]");
        }
        stateMachine.fire(Event.PERIOD_ELAPSED, stock);
    }

    // Lazy init of each stock's state machine
    public void add(Stock stock) {
        stateMachines.put(stock, builder.newStateMachine(State.PASSIVE));
    }

    public State getCurrentState(Stock stock) {
        return stateMachines.containsKey(stock) ? stateMachines.get(stock).getCurrentState() : null;
    }

    public Set<Stock> getStocks() {
        return stateMachines.keySet();
    }

    private WatchlistStateMachine getStateMachine(Stock stock) {
        return stateMachines.containsKey(stock) ? stateMachines.get(stock) : null;
    }

    public static class WatchlistStateMachine extends AbstractStateMachine<WatchlistStateMachine, State, Event, Stock> {

        private DateTime shortListedDate;
        private DateTime currentDate;

        public void setShortListDate(State from, State to, Event event, Stock stock) {
            checkNotNull(stock);
            shortListedDate = stock.hasPrices() ? stock.getLatestPrice().getTimestamp() : null;
        }

        public void removeShortListDate(State from, State to, Event event, Stock stock) {
            shortListedDate = null;
        }

        public void incrementPeriod(State from, State to, Event event, Stock stock) {
            checkNotNull(stock);
            currentDate = currentDate.plus(stock.getPricePeriod());
        }

        public void setCurrentDate(DateTime currentDate) {
            checkNotNull(currentDate);
            this.currentDate = currentDate;
        }

        public DateTime getCurrentDate() {
            return currentDate;
        }

        public DateTime getShortListedDate() {
            return shortListedDate;
        }
    }
}
