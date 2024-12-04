package com.evolveum.midscribe.generator.store;

import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultObjectStore implements ObjectStore {

    private ObjectStore objects;

    private ObjectStore additionalObjects;

    public DefaultObjectStore(ObjectStore objects, ObjectStore additionalObjects) {
        this.objects = objects;
        this.additionalObjects = additionalObjects;
    }

    @Override
    public <T extends ObjectType> T get(Class<T> type, String oid, GetOptions options) {
        T result = objects.get(type, oid, options);
        if (result == null && options.includeAdditionalSources()) {
            result = additionalObjects.get(type, oid, options);
        }

        return result;
    }

    @Override
    public <T extends ObjectType> List<T> list(Class<T> type, GetOptions options) {
        List<T> result = objects.list(type, options);
        if (!options.includeAdditionalSources()) {
            return result;
        }

        List<T> additional = additionalObjects.list(type, options);
        if (additional.isEmpty()) {
            return result;
        }

        List<T> objects = new ArrayList<>();
        objects.addAll(result);
        objects.addAll(additional);

        return Collections.unmodifiableList(objects);
    }
}
