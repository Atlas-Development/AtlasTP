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

public abstract class TPACommand implements CommandExecutor {
    private static final Parameter.Value<ServerPlayer> toPlayer = Parameter.player()
            .key("toplayer")
            .build();

    /**
     * The logger
     */
    protected final Logger logger;

    /**
     * The configuration instance for the plugin.
     */
    protected final AtlasTPConfig config;

    protected final TPAManagerUtil tpaManagerUtil;

    /**
     * The direction of the TPA request
     */
    protected final TPAManager.TPADirection direction;

    /**
     * Constructs a new instance of the TPACommand.
     *
     * @param logger         the logger to log the different command actions
     * @param config         the {@link AtlasTPConfig} instance used to configure the command's behavior.
     * @param tpaManagerUtil the manager for the TPA requests
     * @param direction      the direction in which the TPA should be sent
     */
    public TPACommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil, final TPAManager.TPADirection direction) {
        this.logger = logger;
        this.config = config;
        this.tpaManagerUtil = tpaManagerUtil;
        this.direction = direction;
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
                direction,
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

        return afterCommandExecute(sendingPlayer, fromPlayer, receivingPlayer, toPlayer);
    }

    /**
     * Executes after a TPA request has been successfully sent.
     * (If the player sends an invalid TPA request, then this method won't be called)
     *
     * @param sendingPlayer   The player that sends the TPA request
     * @param fromPlayer      The MiniMessage parameter with the ID {@code fromPlayer} for the sendingPlayer
     * @param receivingPlayer The player that receives the TPA request
     * @param toPlayer        The MiniMessage parameter with the ID {@code toPlayer} for the receivingPlayer
     *
     * @return The standard result of the command
     */
    public abstract CommandResult afterCommandExecute(
            final ServerPlayer sendingPlayer,
            final TagResolver fromPlayer,
            final ServerPlayer receivingPlayer,
            final TagResolver toPlayer
    );

    public static Parameter.Value<ServerPlayer> getToPlayer() {
        return toPlayer;
    }

    public static final class TPARequestCommand extends TPACommand {
        /**
         * Constructs a new instance of the TPACommand.
         *
         * @param logger         the logger to log the different command actions
         * @param config         the {@link AtlasTPConfig} instance used to configure the command's behavior.
         * @param tpaManagerUtil the manager for the TPA requests
         */
        public TPARequestCommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil) {
            super(logger, config, tpaManagerUtil, TPAManager.TPADirection.TO_RECEIVER);
        }

        @Override
        public CommandResult afterCommandExecute(final ServerPlayer sendingPlayer, final TagResolver fromPlayer, final ServerPlayer receivingPlayer, final TagResolver toPlayer) {
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
    }

    public static final class TPAHereCommand extends TPACommand {
        /**
         * Constructs a new instance of the TPACommand.
         *
         * @param logger         the logger to log the different command actions
         * @param config         the {@link AtlasTPConfig} instance used to configure the command's behavior.
         * @param tpaManagerUtil the manager for the TPA requests
         */
        public TPAHereCommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil) {
            super(logger, config, tpaManagerUtil, TPAManager.TPADirection.TO_SENDER);
        }

        @Override
        public CommandResult afterCommandExecute(final ServerPlayer sendingPlayer, final TagResolver fromPlayer, final ServerPlayer receivingPlayer, final TagResolver toPlayer) {
            sendingPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpaHereSendMessage(),
                    fromPlayer,
                    toPlayer
            ));

            receivingPlayer.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpaHereReceiveMessage(),
                    fromPlayer,
                    toPlayer
            ));

            return CommandResult.success();
        }
    }
}
