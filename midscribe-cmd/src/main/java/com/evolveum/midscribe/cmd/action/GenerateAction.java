package com.evolveum.midscribe.cmd.action;

import com.evolveum.midscribe.cmd.Action;
import com.evolveum.midscribe.cmd.ActionResult;
import com.evolveum.midscribe.generator.Generator;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GenerateAction implements Action<GenerateOptions, Void> {

    private GenerateOptions options;

    @Override
    public void init(GenerateOptions options) {
        this.options = options;
    }

    @Override
    public ActionResult<Void> execute() throws Exception {
        Generator generator = new Generator(options);
        generator.generate();

        return new ActionResult<>(null, 0);
    }
}
