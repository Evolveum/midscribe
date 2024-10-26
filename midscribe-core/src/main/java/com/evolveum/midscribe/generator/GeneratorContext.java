package com.evolveum.midscribe.generator;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GeneratorContext {

    private MidPointObjectStore store;
    private GeneratorOptions configuration;

    public GeneratorContext(GeneratorOptions configuration, MidPointObjectStore store) {
        this.configuration = configuration;
        this.store = store;
    }

    public GeneratorOptions getConfiguration() {
        return configuration;
    }

    public MidPointObjectStore getStore() {
        return store;
    }
}
