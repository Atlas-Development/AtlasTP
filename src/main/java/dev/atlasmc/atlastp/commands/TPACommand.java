package dev.atlasmc.atlastp.commands;

import dev.atlasmc.atlastp.config.AtlasTPConfig;
import dev.atlasmc.atlastp.manager.TPAManager;
import dev.atlasmc.atlastp.util.TPAManagerUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public final class TPACommand implements CommandExecutor {
    private static final Parameter.Value<ServerPlayer> toPlayer = Parameter.player()
            .key("toplayer")
            .build();

    /**
     * The logger
     */
    private final Logger logger;

    /**
     * The configuration instance for the plugin.
     */
    private final AtlasTPConfig config;

    private final TPAManagerUtil tpaManagerUtil;

    /**
     * Constructs a new instance of the TPACommand.
     *
     * @param logger the logger to log the different command actions
     * @param config the {@link AtlasTPConfig} instance used to configure the command's behavior.
     */
    public TPACommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil) {
        this.logger = logger;
        this.config = config;
        this.tpaManagerUtil = tpaManagerUtil;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final ServerPlayer receivingPlayer = context.requireOne(toPlayer);

        if(context.cause().root().equals(receivingPlayer)) {
            return CommandResult.error(MiniMessage.miniMessage().deserialize(config.translationStrings().tpaToSelf()));
        }

        final ServerPlayer sendingPlayer = (ServerPlayer)context.cause().root();

        final TagResolver fromPlayer = Placeholder.component("fromplayer", sendingPlayer.displayName().get());
        final TagResolver toPlayer = Placeholder.component("toplayer", receivingPlayer.displayName().get());

        tpaManagerUtil.timedTPARequest(
                sendingPlayer.uniqueId(),
                receivingPlayer.uniqueId(),
                TPAManager.TPADirection.TO_RECEIVER,
                config.tpaExpireTime(),
                (senderID, receiverID) -> {
                    Sponge.server().player(senderID)
                            .ifPresent(serverPlayer -> serverPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                                    config.translationStrings().tpaExpireSender(),
                                    fromPlayer,
                                    toPlayer
                            )));
                    Sponge.server().player(receiverID)
                            .ifPresent(serverPlayer -> serverPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                                    config.translationStrings().tpaExpireReceiver(),
                                    fromPlayer,
                                    toPlayer
                            )));
                }
        );

        sendingPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                config.translationStrings().tpaSendMessage(),
                fromPlayer,
                toPlayer
        ));

        receivingPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                config.translationStrings().tpaReceiveMessage(),
                fromPlayer,
                toPlayer
        ));

        return CommandResult.success();
    }

    public static Parameter.Value<ServerPlayer> getToPlayer() {
        return toPlayer;
    }
}
