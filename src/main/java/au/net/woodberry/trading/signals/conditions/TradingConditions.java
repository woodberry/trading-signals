package au.net.woodberry.trading.signals.conditions;

import org.squirrelframework.foundation.fsm.AnonymousCondition;

public interface TradingConditions<T> {
    
    public AnonymousCondition<T> shouldShortList();
    
    public AnonymousCondition<T> shouldHold();

    public AnonymousCondition<T> shouldEnter();
    
    public AnonymousCondition<T> shouldExit();

}
