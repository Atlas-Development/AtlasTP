package dev.atlasmc.atlastp.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

/**
 * The main configuration class for the AtlasTP plugin.
 * <p>
 * This class is responsible for storing the configuration data required for the plugin.
 * It includes settings such as the configuration version and the messages sent to players.
 * The data is serialized and deserialized using the Configurate library, and all fields
 * annotated with {@code @ConfigSerializable} or {@code @Comment} will be managed
 * automatically by the library.
 * <p>
 * The configuration also supports structured translation strings for various player-facing messages.
 */
@ConfigSerializable
public class AtlasTPConfig {

    /**
     * The version of the configuration file.
     * <p>
     * This version field is intended to track the schema version of the configuration file.
     * It is primarily used to identify and handle breaking changes between different
     * versions of the plugin, ensuring backward compatibility or enabling migration logic.
     * <p>
     * <strong>Note:</strong> This field should not be modified directly by the user.
     */
    @Comment("The version of this configuration file. This should never be changed.")
    private int version = 0;

    /**
     * Retrieves the current configuration version.
     *
     * @return the version of the configuration.
     */
    public int version() {
        return version;
    }

    /**
     * Sets the configuration version.
     *
     * @param version the new version to set.
     */
    public void version(final int version) {
        this.version = version;
    }
}
