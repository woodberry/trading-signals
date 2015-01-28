package au.net.woodberry.trading.signals.examples.watchlist;

import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.model.impl.Stock;
import org.squirrelframework.foundation.fsm.AnonymousAction;

import static au.net.woodberry.trading.signals.examples.watchlist.WatchlistController.WatchlistStateMachine;

public class WatchlistActions {
    
    public static class SetShortListDate extends AnonymousAction<WatchlistStateMachine, State, Event, Stock> {
        @Override
        public void execute(State from, State to, Event event, Stock stock, WatchlistStateMachine stateMachine) {
            stateMachine.setShortListDate(from, to, event, stock);
        }
    }
    
    public static class RemoveShortListedDate extends AnonymousAction<WatchlistStateMachine, State, Event, Stock> {
        @Override
        public void execute(State from, State to, Event event, Stock stock, WatchlistStateMachine stateMachine) {
            stateMachine.removeShortListDate(from, to, event, stock);
        }
    }
    
    public static class IncrementPeriod extends AnonymousAction<WatchlistStateMachine, State, Event, Stock> {
        @Override
        public void execute(State from, State to, Event event, Stock stock, WatchlistStateMachine stateMachine) {
            stateMachine.incrementPeriod(from, to, event, stock);
        }
    }

}
