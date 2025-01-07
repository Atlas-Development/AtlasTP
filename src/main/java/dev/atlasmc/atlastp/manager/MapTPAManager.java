package dev.atlasmc.atlastp.manager;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class MapTPAManager<T> implements TPAManager<T> {
    private final Map<UUID, Map<UUID, TPARequest<T>>> tpaMap = new HashMap<>();

    @Override
    public boolean addRequest(final @NonNull UUID sender, final @NonNull UUID receiver, final @NonNull TPADirection direction, final T data) {
        final Map<UUID, TPARequest<T>> receiverMap = tpaMap.computeIfAbsent(receiver, k -> new HashMap<>());

        return receiverMap.put(sender, new TPARequestImpl<T>(sender, receiver, direction, data)) != null;
    }

    @Override
    public Optional<TPARequest<T>> getRequest(final @NonNull UUID sender, final @NonNull UUID receiver) {
        final Map<UUID, TPARequest<T>> receiverMap = tpaMap.get(receiver);
        if (receiverMap == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(receiverMap.get(sender));
    }

    @Override
    public boolean removeRequest(final @NonNull UUID sender, final @NonNull UUID receiver) {
        final Map<UUID, TPARequest<T>> receiverMap = tpaMap.get(receiver);
        if(receiverMap == null) {
            return false;
        }

        return receiverMap.remove(sender) != null;
    }

    @Override
    public @NonNull Collection<TPARequest<T>> removeRequestsBySender(final @NonNull UUID sender) {
        // TODO
        return List.of();
    }

    @Override
    public @NonNull Collection<TPARequest<T>> removeRequestsToReceiver(final @NonNull UUID receiver) {
        final Map<UUID, TPARequest<T>> receiverMap = tpaMap.remove(receiver);
        if(receiverMap == null) {
            return List.of();
        }

        return receiverMap.values();
    }

    private record TPARequestImpl<T>(UUID sender, UUID receiver, TPADirection direction, T data) implements TPARequest<T> {}
}
