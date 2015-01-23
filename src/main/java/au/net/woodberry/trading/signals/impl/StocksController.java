package au.net.woodberry.trading.signals.impl;

import au.net.woodberry.trading.signals.conditions.TradingConditions;
import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.impl.domain.Stock;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static au.net.woodberry.trading.signals.enums.State.ENTER;
import static au.net.woodberry.trading.signals.enums.State.EXIT;
import static au.net.woodberry.trading.signals.enums.State.HOLD;
import static au.net.woodberry.trading.signals.enums.State.PASSIVE;
import static au.net.woodberry.trading.signals.enums.State.WATCH;

/**
 * A top level class which executes pre-defined trading conditions on a series of stocks  
 */
public class StocksController {
    
    private final TradingConditions<Stock> conditions;
    
    private StateMachineBuilder<StocksStateMachine, State, Event, Stock> builder;
    private Map<Stock, StocksStateMachine> stateMachines;

    /**
     * @param conditions A pre-defined set of conditions from which to generate trading signals (state machine) changes from
     */
    public StocksController(TradingConditions<Stock> conditions) {
        this.builder = StateMachineBuilderFactory.create(StocksStateMachine.class, State.class, Event.class, Stock.class);
        this.stateMachines = new HashMap<>();
        this.conditions = conditions;
        initialize();
    }
    
    private void initialize() {
        builder.externalTransition().from(PASSIVE).to(WATCH).on(Event.SHORT_LISTED).when(conditions.shouldShortList());
        builder.externalTransition().from(WATCH).to(ENTER).on(Event.ENTRY_ALLOWED).when(conditions.shouldEnter());
        builder.externalTransition().from(ENTER).to(HOLD).on(Event.TRADE_ENTERED).when(conditions.shouldHold());
        builder.externalTransition().from(HOLD).to(EXIT).on(Event.STOP_LOSS_BREACHED).when(conditions.shouldExit());
        builder.externalTransition().from(EXIT).to(PASSIVE).on(Event.TRADE_COMPLETED);
        builder.externalTransition().from(EXIT).to(WATCH).on(Event.TRADE_COMPLETED).when(conditions.shouldShortList());
    }
    
    // Lazy init of each stock's state machine
    public void add(Stock stock) {
        stateMachines.put(stock, builder.newStateMachine(PASSIVE));
    }
    
    public State getCurrentState(Stock stock) {
        return stateMachines.containsKey(stock) ? stateMachines.get(stock).getCurrentState() : null;
    }
    
    public Set<Stock> getStocks() {
        return stateMachines.keySet();
    }

    public void execute(Stock stock) {
        StocksStateMachine stateMachine = getStateMachine(stock);
        if (stateMachine == null) {
            throw new RuntimeException("Could not execute trading signal for stock [" + stock + "]. Stock was not found");
        }
        if (stateMachine.getCurrentState() == null) { // Initial state
            stateMachine.fireImmediate(Event.SHORT_LISTED, stock);
        }
        switch (stateMachine.getCurrentState()) {
            case PASSIVE:
                stateMachine.fire(Event.SHORT_LISTED, stock);
                break;
            case WATCH:
                stateMachine.fire(Event.ENTRY_ALLOWED, stock);
                break;
            case ENTER:
                stateMachine.fire(Event.TRADE_ENTERED, stock);
                break;
            case HOLD:
                stateMachine.fire(Event.STOP_LOSS_BREACHED, stock);
                break;
            case EXIT:
                stateMachine.fire(Event.TRADE_COMPLETED, stock);
                break;
            default:
                throw new RuntimeException("Unexpected state [" + stateMachine.getCurrentState() + "]");
        }
    }
    
    private StocksStateMachine getStateMachine(Stock stock) {
        return stateMachines.containsKey(stock) ? stateMachines.get(stock) : null;
    }
}
