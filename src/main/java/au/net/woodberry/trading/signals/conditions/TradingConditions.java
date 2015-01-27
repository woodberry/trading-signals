package au.net.woodberry.trading.signals.conditions;

import org.squirrelframework.foundation.fsm.AnonymousCondition;

public interface TradingConditions<T> {

    /**
     * Setup a condition to short list - used in the context of setting up conditionals inside an FSM
     */
    public AnonymousCondition<T> shouldShortList();

    /**
     * Setup a condition to hold a position - used in the context of setting up conditionals inside an FSM
     */
    public AnonymousCondition<T> shouldHold();

    /**
     * Setup a condition to enter a position - used in the context of setting up conditionals inside an FSM
     */
    public AnonymousCondition<T> shouldEnter();

    /**
     * Setup a condition to exit a position - used in the context of setting up conditionals inside an FSM
     */
    public AnonymousCondition<T> shouldExit();

}
