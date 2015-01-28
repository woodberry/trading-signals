package au.net.woodberry.trading.signals.examples.watchlist;

import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.model.impl.Stock;
import org.squirrelframework.foundation.fsm.AnonymousAction;

public class WatchlistActions {
    
    public static class SetShortListDate extends AnonymousAction<WatchlistController.WatchlistStateMachine, State, Event, Stock> {
        @Override
        public void execute(State from, State to, Event event, Stock stock, WatchlistController.WatchlistStateMachine stateMachine) {
            stateMachine.setShortListDate(from, to, event, stock);
        }
    }
    
    public static class RemoveShortListedDate extends AnonymousAction<WatchlistController.WatchlistStateMachine, State, Event, Stock> {
        @Override
        public void execute(State from, State to, Event event, Stock stock, WatchlistController.WatchlistStateMachine stateMachine) {
            stateMachine.removeShortListDate(from, to, event, stock);
        }
    }
    
    public static class IncrementPeriod extends AnonymousAction<WatchlistController.WatchlistStateMachine, State, Event, Stock> {
        @Override
        public void execute(State from, State to, Event event, Stock stock, WatchlistController.WatchlistStateMachine stateMachine) {
            stateMachine.incrementPeriod(from, to, event, stock);
        }
    }

}
