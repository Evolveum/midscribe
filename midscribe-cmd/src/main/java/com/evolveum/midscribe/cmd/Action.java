package com.evolveum.midscribe.cmd;

/**
 * Created by Viliam Repan (lazyman).
 */
public interface Action<O, R> {

    void init(O options) throws Exception;

    ActionResult<R> execute() throws Exception;
}
