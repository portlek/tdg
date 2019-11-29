package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.abs.TDGEvent;

public interface Acceptable<X extends TDGEvent> {

    void accept(X event);

}
