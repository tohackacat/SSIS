package org.example.tui;

public sealed interface ControlFlow
        permits ControlFlow.Poll,
        ControlFlow.Wait,
        ControlFlow.WaitUntil,
        ControlFlow.Exit {

    record Poll() implements ControlFlow {
    }

    record Wait() implements ControlFlow {
    }

    record WaitUntil(long deadlineNanos) implements ControlFlow {
    }

    record Exit() implements ControlFlow {
    }
}
