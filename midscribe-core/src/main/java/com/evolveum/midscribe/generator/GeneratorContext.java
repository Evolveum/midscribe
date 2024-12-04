package com.evolveum.midscribe.generator;

import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midscribe.generator.store.ObjectStore;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GeneratorContext {

    private GeneratorOptions configuration;
    private PrismContext prismContext;
    private ObjectStore store;

    public GeneratorContext(GeneratorOptions configuration, PrismContext prismContext, ObjectStore store) {
        this.configuration = configuration;
        this.prismContext = prismContext;
        this.store = store;
    }

    public PrismContext getPrismContext() {
        return prismContext;
    }

    public GeneratorOptions getConfiguration() {
        return configuration;
    }

    public ObjectStore getStore() {
        return store;
    }
}
