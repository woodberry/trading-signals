package au.net.woodberry.trading.signals.impl;

import au.net.woodberry.trading.signals.enums.Event;
import au.net.woodberry.trading.signals.enums.State;
import au.net.woodberry.trading.signals.impl.domain.Stock;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class StocksStateMachine extends AbstractStateMachine<StocksStateMachine, State, Event, Stock> {

}
