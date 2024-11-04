package com.evolveum.midscribe.generator.store;

import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InMemoryObjectStore implements ObjectStore {

    private final Map<Class<? extends ObjectType>, List<ObjectType>> objects;

    public InMemoryObjectStore(@NotNull Map<Class<? extends ObjectType>, List<ObjectType>> objects) {
        this.objects = objects;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ObjectType> @NotNull List<T> list(@NotNull Class<T> type, @NotNull GetOptions options) {
        return (List<T>) objects.getOrDefault(type, Collections.emptyList());
    }

    @Override
    public <T extends ObjectType> @Nullable T get(@NotNull Class<T> type, @NotNull String oid, @NotNull GetOptions options) {
        List<T> list = list(type, options);
        for (T t : list) {
            if (Objects.equals(oid, t.getOid())) {
                return t;
            }
        }

        return null;
    }
}
