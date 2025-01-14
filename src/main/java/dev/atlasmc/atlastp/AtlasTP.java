package dev.atlasmc.atlastp;

import com.google.inject.Inject;
import dev.atlasmc.atlastp.commands.TPACommand;
import dev.atlasmc.atlastp.commands.TPAResponseCommand;
import dev.atlasmc.atlastp.commands.TPCommand;
import dev.atlasmc.atlastp.config.AtlasTPConfig;
import dev.atlasmc.atlastp.manager.MapTPAManager;
import dev.atlasmc.atlastp.util.TPAManagerUtil;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

/**
 * The main class of your Sponge plugin.
 *
 * <p>All methods are optional -- some common event registrations are included as a jumping-off point.</p>
 */
@Plugin("atlastp")
public class AtlasTP {

    private final ConfigurationReference<CommentedConfigurationNode> reference;

    private final PluginContainer container;
    private final Logger logger;

    private ValueReference<AtlasTPConfig, CommentedConfigurationNode> config;

    private TPAManagerUtil tpaManagerUtil;

    @Inject
    AtlasTP(final PluginContainer container, final Logger logger, final @DefaultConfig(sharedRoot = true) ConfigurationReference<CommentedConfigurationNode> reference) {
        this.container = container;
        this.logger = logger;

        this.reference = reference;
    }

    @Listener
    public void onConstructPlugin(final ConstructPluginEvent event) throws ConfigurateException {
        // Perform any one-time setup
        this.logger.info("Constructing AtlasTP");

        this.logger.info("Loading configuration");
        this.config = this.reference.referenceTo(AtlasTPConfig.class);

        if(this.config.get().version() != 0) {
            this.logger.error(() -> String.format(
                        "The config version was expected to be 0, but got %d. This version does not exist.",
                        this.config.get().version()
                    )
            );

            this.logger.error("The version of the config should NOT be changed.");

            // Throwing an exception so that the plugin gets removed from the plugin list
            // and a large red message is being sent, which alarms the user.
            throw new RuntimeException("Invalid version provided. The plugin cannot recover.");
        }

        this.reference.save();

        this.logger.info("Setting up the TPManager");
        this.tpaManagerUtil = new TPAManagerUtil(Sponge.pluginManager().fromInstance(this).orElseThrow(), new MapTPAManager<>());
    }

    @Listener
    public void onServerStarting(final StartingEngineEvent<Server> event) {
        // Any setup per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        // Any tear down per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.
    }

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        logger.info("Initializing commands");

        // When possible, all commands should be registered within a command register event
        event.register(
                this.container,
                Command.builder()
                    .addParameters(TPCommand.getFirstParam(), TPCommand.getSecondParam())
                    .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                    .permission("atlastp.command.tp")
                    .executor(new TPCommand(logger, config.get())).build(),
                "tp",
                "teleport"
        );

        event.register(
                this.container,
                Command.builder()
                        .addParameter(TPACommand.TPARequestCommand.getToPlayer())
                        .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                        .permission("atlastp.command.tpa")
                        .executor(new TPACommand.TPARequestCommand(logger, config.get(), tpaManagerUtil)).build(),
                "tpask",
                "tpa"
        );

        event.register(
                this.container,
                Command.builder()
                        .addParameter(TPACommand.TPAHereCommand.getToPlayer())
                        .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                        .permission("atlastp.command.tpahere")
                        .executor(new TPACommand.TPAHereCommand(logger, config.get(), tpaManagerUtil)).build(),
                "tpahere",
                "tpah"
        );

        // TPAResponse commands
        event.register(
                this.container,
                Command.builder()
                        .addParameter(TPAResponseCommand.selectedPlayer())
                        .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                        .permission("atlastp.command.tparesponse.tpaccept")
                        .executor(new TPAResponseCommand.TPAllowCommand(logger, config.get(), tpaManagerUtil)).build(),
                "tpaccept",
                "tpaaccept",
                "tpallow"
        );

        event.register(
                this.container,
                Command.builder()
                        .addParameter(TPAResponseCommand.selectedPlayer())
                        .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                        .permission("atlastp.command.tparesponse.tpdeny")
                        .executor(new TPAResponseCommand.TPDenyCommand(logger, config.get(), tpaManagerUtil)).build(),
                "tpdeny",
                "tpadeny",
                "tpareject"
        );
    }
}
