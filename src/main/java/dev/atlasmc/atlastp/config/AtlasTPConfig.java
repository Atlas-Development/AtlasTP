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

        /**
         * The message displayed when a player uses {@code /tp <player>}.
         * <p>
         * Placeholders:
         * <ul>
         *   <li>{@code <executor>} - Name of the player who executed the command.</li>
         *   <li>{@code <toplayer>} - Name of the player being teleported to.</li>
         * </ul>
         */
        @Comment("""
                Message for /tp <player>.
                
                Allows for <executor> (Name of player that send the tp command)
                and <toplayer> (Name of the player that was teleported to)
                """)
        private String tpToMessage = "Teleporting you to <toplayer>.";

        /**
         * The message displayed when a player uses {@code /tp <fromplayer> <toplayer>}.
         * <p>
         * Placeholders:
         * <ul>
         *   <li>{@code <executor>} - Name of the player who executed the command.</li>
         *   <li>{@code <fromplayer>} - Name of the player being teleported.</li>
         *   <li>{@code <toplayer>} - Name of the target player being teleported to.</li>
         * </ul>
         */
        @Comment("""
                Message for /tp <fromplayer> <toplayer>
                
                Allows for <executor> (Name of player that send the tp command),
                <fromplayer> (Name of the player that is being teleported)
                and <toplayer> (Name of the player that is being teleported to)
                """)
        private String tpOtherMessage = "Teleporting <fromplayer> to <toplayer>";

        /**
         * The message sent to the player being teleported via {@code /tp <fromplayer> <toplayer>}.
         * <p>
         * Placeholders:
         * <ul>
         *   <li>{@code <executor>} - Name of the player who executed the command.</li>
         *   <li>{@code <fromplayer>} - Name of the player being teleported.</li>
         *   <li>{@code <toplayer>} - Name of the target player being teleported to.</li>
         * </ul>
         */
        @Comment("""
                Message to the player that is being teleported by /tp <fromplayer> <toplayer>.
                
                Allows for <executor> (Name of player that send the tp command),
                <fromplayer> (Name of the player that is being teleported)
                and <toplayer> (Name of the player that is being teleported to)
                """)
        private String beingTeleportedToMessage = "You were teleported to <toplayer> by <executor>";

        /**
         * Retrieves the message for {@code /tp <player>}.
         *
         * @return the teleport-to message template.
         */
        public String tpToMessage() {
            return this.tpToMessage;
        }

        /**
         * Sets the message for {@code /tp <player>}.
         *
         * @param tpToMessage the message template to set.
         */
        public void tpToMessage(final String tpToMessage) {
            this.tpToMessage = tpToMessage;
        }

        /**
         * Retrieves the message for {@code /tp <fromplayer> <toplayer>}.
         *
         * @return the teleport-other message template.
         */
        public String tpOtherMessage() {
            return this.tpOtherMessage;
        }

        /**
         * Sets the message for {@code /tp <fromplayer> <toplayer>}.
         *
         * @param tpOtherMessage the message template to set.
         */
        public void tpOtherMessage(final String tpOtherMessage) {
            this.tpOtherMessage = tpOtherMessage;
        }

        /**
         * Retrieves the message sent to a player being teleported by another player.
         *
         * @return the message template for being teleported.
         */
        public String beingTeleportedToMessage() {
            return this.beingTeleportedToMessage;
        }

        /**
         * Sets the message sent to a player being teleported by another player.
         *
         * @param beingTeleportedToMessage the message template to set.
         */
        public void beingTeleportedToMessage(final String beingTeleportedToMessage) {
            this.beingTeleportedToMessage = beingTeleportedToMessage;
        }
    }
}
