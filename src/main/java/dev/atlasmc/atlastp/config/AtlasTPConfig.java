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
     * Contains the translation strings for various messages sent to players by the plugin.
     * <p>
     * This field encapsulates all the customizable messages that are displayed to players.
     * It supports placeholders such as {@code <executor>}, {@code <toplayer>}, and {@code <fromplayer>},
     * which are dynamically replaced during runtime based on the command context.
     */
    @Comment("The different translation strings for player messages.")
    private @NonNull TranslationStrings translationStrings = new TranslationStrings();

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

    /**
     * Retrieves the translation strings for player messages.
     *
     * @return the {@link TranslationStrings} object containing message templates.
     */
    public TranslationStrings translationStrings() {
        return this.translationStrings;
    }

    /**
     * Sets the translation strings for player messages.
     *
     * @param translationStrings the {@link TranslationStrings} object to set.
     * @throws NullPointerException if {@code translationStrings} is {@code null}.
     */
    public void translationStrings(final @NonNull TranslationStrings translationStrings) {
        this.translationStrings = translationStrings;
    }

    /**
     * A nested class that defines customizable messages sent to players.
     * <p>
     * This class allows server administrators to configure the language and format
     * of messages displayed to players during teleportation commands.
     */
    @ConfigSerializable
    public static class TranslationStrings {

    }
}
