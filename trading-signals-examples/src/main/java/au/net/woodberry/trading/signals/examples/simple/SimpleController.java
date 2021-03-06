package au.net.woodberry.trading.signals.examples.simple;

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

import static au.net.woodberry.trading.signals.enums.State.ENTER;
import static au.net.woodberry.trading.signals.enums.State.EXIT;
import static au.net.woodberry.trading.signals.enums.State.HOLD;
import static au.net.woodberry.trading.signals.enums.State.PASSIVE;
import static au.net.woodberry.trading.signals.enums.State.WATCH;

/**
 * A top level class which executes pre-defined trading conditions on a series of stocks  
 */
public class SimpleController {
    
    private final TradingConditions<Stock> conditions;
    
    private StateMachineBuilder<SimpleStateMachine, State, Event, Stock> builder;
    private Map<Stock, SimpleStateMachine> stateMachines;

    /**
     * @param conditions A pre-defined set of conditions from which to generate trading nop (state machine) changes from
     */
    public SimpleController(TradingConditions<Stock> conditions) {
        this.builder = StateMachineBuilderFactory.create(SimpleStateMachine.class, State.class, Event.class, Stock.class);
        this.stateMachines = new HashMap<>();
        this.conditions = conditions;
        initializeBuilder(); // The internal transition / fsm logic...
    }
    
    private void initializeBuilder() {
        builder.externalTransition().from(PASSIVE).to(WATCH).on(Event.SHORT_LISTED).when(conditions.shouldShortList());
        builder.externalTransition().from(WATCH).to(ENTER).on(Event.ENTRY_ALLOWED).when(conditions.shouldEnter());
        builder.externalTransition().from(WATCH).to(PASSIVE).on(Event.HAS_EXPIRED).when(conditions.shouldNoLongerShortList());
        builder.externalTransition().from(ENTER).to(HOLD).on(Event.TRADE_ENTERED).when(conditions.shouldHold());
        builder.externalTransition().from(HOLD).to(EXIT).on(Event.STOP_LOSS_BREACHED).when(conditions.shouldExit());
        builder.externalTransition().from(EXIT).to(PASSIVE).on(Event.TRADE_COMPLETED).when(conditions.shouldComplete());
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
        SimpleStateMachine stateMachine = getStateMachine(stock);
        if (stateMachine == null) {
            throw new RuntimeException("Could not execute trading signal for stock [" + stock + "]. Stock was not found");
        }
        if (stateMachine.getCurrentState() == null) { // Initial state
            stateMachine.start();
        }
        switch (stateMachine.getCurrentState()) {
            case PASSIVE:
                stateMachine.fire(Event.SHORT_LISTED, stock);
                break;
            case WATCH:
                stateMachine.fire(Event.HAS_EXPIRED, stock);
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
    
    private SimpleStateMachine getStateMachine(Stock stock) {
        return stateMachines.containsKey(stock) ? stateMachines.get(stock) : null;
    }

    private static class SimpleStateMachine extends AbstractStateMachine<SimpleStateMachine, State, Event, Stock> {

    }
}
