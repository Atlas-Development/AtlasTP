package dev.atlasmc.atlastp.commands;

import dev.atlasmc.atlastp.config.AtlasTPConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

/**
 * Handles the teleportation commands for the AtlasTP plugin.
 * <p>
 * Supports teleporting a player to another player or teleporting one player to another
 * by a third-party executor.
 */
public final class TPCommand implements CommandExecutor {

    /**
     * Represents the parameter for the first player.
     */
    private static final Parameter.Value<ServerPlayer> firstParam = Parameter.player()
            .key("firstplayer")
            .build();

    /**
     * Represents the optional parameter for the second player, used for third-party teleportation.
     */
    private static final Parameter.Value<ServerPlayer> secondParam = Parameter.player()
            .key("secondplayer")
            .requiredPermission("atlastp.command.tpother")
            .optional()
            .build();

    /**
     * The logger
     */
    private final Logger logger;

    /**
     * The configuration instance for the plugin.
     */
    private final AtlasTPConfig config;

    /**
     * Constructs a new instance of the TPCommand.
     *
     * @param logger the logger to log the different command actions
     * @param config the {@link AtlasTPConfig} instance used to configure the command's behavior.
     */
    public TPCommand(final Logger logger, final AtlasTPConfig config) {
        this.logger = logger;
        this.config = config;
    }

    /**
     * Executes the teleportation command.
     *
     * @param context the command context containing command parameters and execution details.
     * @return the result of the command execution.
     * @throws CommandException if an error occurs during execution.
     */
    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        if (context.hasAny(secondParam)) {
            return executeTwoPlayers(context);
        }

        // Single-player teleportation
        final ServerPlayer player = (ServerPlayer) context.cause().root();
        final ServerPlayer toPlayer = context.requireOne(firstParam);

        if(player.equals(toPlayer)) {
            context.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpToSelf(),
                    Placeholder.component("player", player.displayName().get())
            ));
            return CommandResult.success();
        }

        // Teleport the executing player to the target player
        player.user().setLocation(toPlayer.world().key(), toPlayer.position());

        // Send a confirmation message to the executing player
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                config.translationStrings().tpToMessage(),
                Placeholder.component("executor", player.displayName().get()),
                Placeholder.component("toplayer", toPlayer.displayName().get())
        ));

        logger.info("Teleporting " + player.user().name() + " to " + toPlayer.user().name());

        return CommandResult.success();
    }

    /**
     * Handles teleportation of one player to another by a third-party executor.
     *
     * @param context the command context containing the players to be teleported.
     * @return the result of the command execution.
     * @throws CommandException if an error occurs during execution.
     */
    private CommandResult executeTwoPlayers(final CommandContext context) throws CommandException {
        final ServerPlayer player = context.requireOne(firstParam);
        final ServerPlayer toPlayer = context.requireOne(secondParam);

        if(player.equals(toPlayer)) {
            context.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpOtherToThemselves(),
                    Placeholder.component("player", player.displayName().get())
            ));
            return CommandResult.success();
        }

        // Teleport the player to the target player
        player.user().setLocation(toPlayer.world().key(), toPlayer.position());

        // Send a message to the executor, if applicable
        if (config.translationStrings().tpOtherMessage() != null) {
            context.cause().sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().tpOtherMessage(),
                    Placeholder.component("executor", ((ServerPlayer) context.cause().root()).displayName().get()),
                    Placeholder.component("fromplayer", player.displayName().get()),
                    Placeholder.component("toplayer", toPlayer.displayName().get())
            ));
        }

        // Notify the teleported player, if applicable
        if (config.translationStrings().beingTeleportedToMessage() != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    config.translationStrings().beingTeleportedToMessage(),
                    Placeholder.component("executor", ((ServerPlayer) context.cause().root()).displayName().get()),
                    Placeholder.component("fromplayer", player.displayName().get()),
                    Placeholder.component("toplayer", toPlayer.displayName().get())
            ));
        }

        logger.info("Teleporting " + player.user().name() + " to " + toPlayer.user().name() + " (teleported by " + ((ServerPlayer) context.cause().root()).user().name() + ")");

        return CommandResult.success();
    }

    /**
     * Retrieves the parameter for the first player.
     *
     * @return the parameter for the first player.
     */
    public static Parameter.Value<ServerPlayer> getFirstParam() {
        return firstParam;
    }

    /**
     * Retrieves the parameter for the second player.
     *
     * @return the parameter for the second player.
     */
    public static Parameter.Value<ServerPlayer> getSecondParam() {
        return secondParam;
    }
}
