package com.evolveum.midscribe.generator;

import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midscribe.generator.store.ObjectStore;

@FunctionalInterface
public interface ObjectStoreFactory {

    ObjectStore createObjectStore(GeneratorOptions options, PrismContext prismContext, boolean additional);
}
