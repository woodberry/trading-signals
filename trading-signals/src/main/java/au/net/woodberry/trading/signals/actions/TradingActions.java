package au.net.woodberry.trading.signals.actions;

import org.squirrelframework.foundation.fsm.AnonymousAction;

public interface TradingActions<T extends AnonymousAction> {

    public T shortListedActions();
    
    public T hasExpiredActions();
    
    public T periodElapsedActions();
    
    public T stopLossBreachedActions();
    
    public T entryAllowedActions();

    public T tradeEnteredActions();

    public T tradeCompletedActions();
}
