package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.abs.TDGEvent;

public interface BiAcceptable<X extends TDGEvent, Y extends TDGEvent> extends Acceptable<X> {

    void exec(Y event);

}
