package dev.atlasmc.atlastp.manager;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TPAManager<T> {
    boolean addRequest(final @NonNull UUID sender, final @NonNull UUID receiver, final @NonNull TPADirection direction, final T data);

    Optional<TPARequest<T>> getRequest(final @NonNull UUID sender, final @NonNull UUID receiver);

    boolean removeRequest(final @NonNull UUID sender, final @NonNull UUID receiver);

    @NonNull Collection<TPARequest<T>> removeRequestsBySender(final @NonNull UUID sender);

    @NonNull Collection<TPARequest<T>> removeRequestsToReceiver(final @NonNull UUID receiver);

    interface TPARequest<T> {
        UUID sender();

        UUID receiver();

        TPADirection direction();

        T data();
    }

    enum TPADirection {
        TO_SENDER,
        TO_RECEIVER
    }
}
