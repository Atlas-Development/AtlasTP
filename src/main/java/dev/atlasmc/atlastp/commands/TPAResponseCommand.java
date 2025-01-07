package dev.atlasmc.atlastp.commands;

import dev.atlasmc.atlastp.config.AtlasTPConfig;
import dev.atlasmc.atlastp.manager.TPAManager;
import dev.atlasmc.atlastp.util.TPAManagerUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;

import java.util.Optional;

public abstract class TPAResponseCommand implements CommandExecutor {
    private static final Parameter.Value<ServerPlayer> selectedPlayer = Parameter.player()
            .key("selectedplayer")
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

    public TPAResponseCommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil) {
        this.logger = logger;
        this.config = config;
        this.tpaManagerUtil = tpaManagerUtil;
    }

    public static Parameter.Value<ServerPlayer> selectedPlayer() {
        return selectedPlayer;
    }

    public static final class TPAllowCommand extends TPAResponseCommand {
        public TPAllowCommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil) {
            super(logger, config, tpaManagerUtil);
        }

        @Override
        public CommandResult execute(final CommandContext context) throws CommandException {
            final ServerPlayer player = context.requireOne(selectedPlayer);

            final ServerPlayer commandSender = ((ServerPlayer)context.cause().root());

            final TagResolver fromPlayer = Placeholder.component("fromplayer", player.displayName().get());
            final TagResolver toPlayer = Placeholder.component("toplayer", commandSender.displayName().get());

            final Optional<TPAManager.TPARequest<ScheduledTask>> request = tpaManagerUtil.removeRequest(player.uniqueId(), commandSender.uniqueId());
            if(request.isEmpty()) {
                context.sendMessage(MiniMessage.miniMessage().deserialize(
                        config.translationStrings().noOpenTPARequest(),
                        fromPlayer,
                        toPlayer
                ));
                return CommandResult.success();
            }

            switch (request.get().direction()) {
                case TO_SENDER:
                    commandSender.setLocationAndRotation(player.serverLocation(), player.rotation());
                    break;
                case TO_RECEIVER:
                    player.setLocationAndRotation(commandSender.serverLocation(), commandSender.rotation());
                    break;
                default:
                    return CommandResult.error(
                            Component.text("Somehow the direction for a TPA is " + request.get().direction())
                    );
            }

            commandSender.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpaAcceptReceiver(),
                    fromPlayer,
                    toPlayer
            ));
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpaAcceptSender(),
                    fromPlayer,
                    toPlayer
            ));

            return CommandResult.success();
        }
    }

    public static final class TPDenyCommand extends TPAResponseCommand {
        public TPDenyCommand(final Logger logger, final AtlasTPConfig config, final TPAManagerUtil tpaManagerUtil) {
            super(logger, config, tpaManagerUtil);
        }

        @Override
        public CommandResult execute(final CommandContext context) throws CommandException {
            final ServerPlayer player = context.requireOne(selectedPlayer);

            final ServerPlayer commandSender = (ServerPlayer) context.cause().root();

            final TagResolver fromPlayer = Placeholder.component("fromplayer", player.displayName().get());
            final TagResolver toPlayer = Placeholder.component("toplayer", commandSender.displayName().get());

            boolean exists = tpaManagerUtil.removeRequest(player.uniqueId(), commandSender.uniqueId()).isPresent();
            if(!exists) {
                context.sendMessage(MiniMessage.miniMessage().deserialize(
                        config.translationStrings().noOpenTPARequest(),
                        fromPlayer,
                        toPlayer
                ));

                return CommandResult.success();
            }

            commandSender.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpaDeclineReceiver(),
                    fromPlayer,
                    toPlayer
            ));
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpaDeclineSender(),
                    fromPlayer,
                    toPlayer
            ));

            return CommandResult.success();
        }
    }
}
