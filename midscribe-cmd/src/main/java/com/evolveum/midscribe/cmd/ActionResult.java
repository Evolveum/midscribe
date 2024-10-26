package com.evolveum.midscribe.cmd;

public class ActionResult<T> {

    private final T result;

    private final int exitCode;

    private final String exitMessage;

    public ActionResult(T result) {
        this(result, 0);
    }

    public ActionResult(T result, int exitCode) {
        this(result, exitCode, null);
    }

    public ActionResult(T result, int exitCode, String exitMessage) {
        this.result = result;
        this.exitCode = exitCode;
        this.exitMessage = exitMessage;
    }

    public T result() {
        return result;
    }

    public int exitCode() {
        return exitCode;
    }

    public String exitMessage() {
        return exitMessage;
    }
}
