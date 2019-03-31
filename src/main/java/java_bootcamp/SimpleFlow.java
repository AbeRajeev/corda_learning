package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;

public class SimpleFlow extends FlowLogic<Void> {
    @Suspendable
    public Void call() throws FlowException {
        return null;
    }
}
