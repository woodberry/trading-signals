package au.net.woodberry.trading.signals.examples.simple;

import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.model.Stock;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class SimpleStocksStateMachine extends AbstractStateMachine<SimpleStocksStateMachine, State, Event, Stock> {

}
