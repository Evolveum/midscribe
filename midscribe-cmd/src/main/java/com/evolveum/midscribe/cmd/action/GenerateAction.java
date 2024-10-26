package com.evolveum.midscribe.cmd.action;

import com.evolveum.midscribe.cmd.Action;
import com.evolveum.midscribe.cmd.ActionResult;
import com.evolveum.midscribe.generator.GenerateOptions;
import com.evolveum.midscribe.generator.Generator;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GenerateAction implements Action<GenerateActionOptions, Void> {

    private GenerateActionOptions options;

    @Override
    public void init(GenerateActionOptions options) {
        this.options = options;
    }

    @Override
    public ActionResult<Void> execute() throws Exception {
        GenerateOptions options = buildGeneratorOptions();

        Generator generator = new Generator(options);
        generator.generate();

        return new ActionResult<>(null, 0);
    }

    private GenerateOptions buildGeneratorOptions() {
        GenerateOptions opts = new GenerateOptions();
        opts.setSourceDirectory(options.getSource());
//        opts.setAdditionalSource(options.getAdditionalSource());  // todo implement
        opts.setInclude(options.getInclude());
        opts.setExclude(options.getExclude());
        opts.setExportOutput(options.getOutput());
        opts.setExportFormat(options.getOutputFormat().getFormat());
        opts.setTemplate(options.getTemplate());
        opts.setProperties(options.getProperties());

        return opts;
    }
}
