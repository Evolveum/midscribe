package com.evolveum.midscribe.generator.store;

import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ObjectStore {

    <T extends ObjectType> @NotNull List<T> list(@NotNull Class<T> type, @NotNull GetOptions options);

    <T extends ObjectType> @Nullable T get(@NotNull Class<T> type, @NotNull String oid, @NotNull GetOptions options);
}
