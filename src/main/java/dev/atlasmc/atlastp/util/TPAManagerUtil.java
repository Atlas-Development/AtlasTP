package dev.atlasmc.atlastp.util;

import dev.atlasmc.atlastp.manager.TPAManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public final class TPAManagerUtil {
    private final PluginContainer pluginContainer;
    private final TPAManager<ScheduledTask> tpaManager;

    public TPAManagerUtil(final PluginContainer pluginContainer, final TPAManager<ScheduledTask> tpaManager) {
        this.pluginContainer = pluginContainer;
        this.tpaManager = tpaManager;
    }

    public boolean timedTPARequest(
            final UUID sender,
            final UUID receiver,
            final TPAManager.TPADirection direction,
            final long ticks,
            final BiConsumer<UUID, UUID> consumer
    ) {
        tpaManager.getRequest(sender, receiver)
                .ifPresent(scheduledTaskTPARequest -> scheduledTaskTPARequest.data().cancel());

        final ScheduledTask task = Sponge.asyncScheduler().submit(
                Task.builder()
                        .delay(Ticks.of(ticks))
                        .execute(() -> {
                            if(tpaManager.removeRequest(sender, receiver)) {
                                consumer.accept(sender, receiver);
                            }
                        })
                        .plugin(pluginContainer)
                        .build()
        );

        return tpaManager.addRequest(sender, receiver, direction, task);
    }

    public Optional<TPAManager.TPARequest<ScheduledTask>> removeRequest(final UUID sender, final UUID receiver) {
        final Optional<TPAManager.TPARequest<ScheduledTask>> request = tpaManager.getRequest(sender, receiver);

        if(request.isEmpty())
            return Optional.empty();

        request.get().data().cancel();

        tpaManager.removeRequest(sender, receiver);

        return request;
    }

    public TPAManager<ScheduledTask> getTpaManager() {
        return this.tpaManager;
    }
}
