package au.net.woodberry.trading.signals.examples.movingaverage;

import au.net.woodberry.trading.signals.conditions.TradingConditions;
import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.model.Stock;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class MovingAverageController {

    private final StateMachineBuilder<MovingAverageStateMachine, State, Event, Stock> builder;
    private final TradingConditions<Stock> conditions;
    private Map<Stock, MovingAverageStateMachine> stateMachines;
    
    public MovingAverageController(TradingConditions<Stock> conditions) {
        this.builder = StateMachineBuilderFactory.create(MovingAverageStateMachine.class, State.class, Event.class, Stock.class);
        this.stateMachines = new HashMap<>();
        this.conditions = conditions;
        initializeBuilder(); // The internal transition / fsm logic...
    }

    private void initializeBuilder() {
        builder.externalTransition().from(State.PASSIVE).to(State.ENTER).on(Event.ENTRY_ALLOWED).when(conditions.shouldEnter());
        builder.externalTransition().from(State.ENTER).to(State.HOLD).on(Event.TRADE_ENTERED);
        builder.externalTransition().from(State.HOLD).to(State.EXIT).on(Event.STOP_LOSS_BREACHED).when(conditions.shouldExit());
        builder.externalTransition().from(State.EXIT).to(State.PASSIVE).on(Event.TRADE_COMPLETED);
    }

    public void execute(Stock stock) {
        MovingAverageStateMachine stateMachine = getStateMachine(stock);
        checkNotNull(stock, "Supplied input Stock is invalid: NULL");
        checkNotNull(stateMachine, "Could not execute trading signal for stock [" + stock + "]. Stock was not found");
        if (!stateMachine.isStarted()) {
            stateMachine.start();
        }
        switch (stateMachine.getCurrentState()) {
            case PASSIVE:
                stateMachine.fire(Event.ENTRY_ALLOWED, stock);
                break;
            case ENTER:
                stateMachine.fire(Event.TRADE_ENTERED);
                break;
            case HOLD:
                stateMachine.fire(Event.STOP_LOSS_BREACHED, stock);
                break;
            case EXIT:
                stateMachine.fire(Event.TRADE_COMPLETED);
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

    private MovingAverageStateMachine getStateMachine(Stock stock) {
        return stateMachines.containsKey(stock) ? stateMachines.get(stock) : null;
    }

    public static class MovingAverageStateMachine extends AbstractStateMachine<MovingAverageStateMachine, State, Event, Stock> {

    }
}
