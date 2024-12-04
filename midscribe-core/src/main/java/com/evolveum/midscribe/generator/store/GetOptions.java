package com.evolveum.midscribe.generator.store;

public class GetOptions {

    private boolean includeAdditionalSources;

    public boolean includeAdditionalSources() {
        return includeAdditionalSources;
    }

    public GetOptions includeAdditionalSources(boolean includeAdditionalSources) {
        this.includeAdditionalSources = includeAdditionalSources;
        return this;
    }

    public static GetOptions createIncludeAdditionalSources() {
        return new GetOptions().includeAdditionalSources(true);
    }

    public static GetOptions create() {
        return new GetOptions();
    }
}
