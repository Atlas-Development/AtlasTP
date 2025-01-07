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

        @Comment("""
                Message to the player if they try to teleport to themselves with the /tp command.
                
                Allows for <player> (Name of the player that is being teleported)
                """)
        private String tpToSelf = "You cannot teleport to yourself! Try teleporting to someone else.";

        @Comment("""
                Message to the player if they try to teleport another player to themselves with the /tp <player> <player> command.
                
                Allows for <player> (Name of the player that is being teleported)
                """)
        private String tpOtherToThemselves = "You cannot teleport the player <player> to themselves.";

        @Comment("Message to the player if they try to send a teleport request to themselves with the /tpa command.")
        private String tpaToSelf = "You can't send a teleport request to yourself! Try sending a request to someone else.";

        @Comment("""
                Message to the player that sends the TPA request.
                
                Allows for <fromplayer> (Name of the player that sends the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaSendMessage = "Sending teleport request to <toplayer>.";

        @Comment("""
                Message to the player that receives the TPA request.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaReceiveMessage = "You got a teleport request from <fromplayer>.\n" +
                "To accept, type /tpaccept <fromplayer> and to reject, type /tpdeny <fromplayer>.";

        @Comment("""
                Message to the sender when the teleport request expires.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaExpireSender = "Your teleport request to <toplayer> has expired.";

        @Comment("""
                Message to the receiver when the teleport request expires.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaExpireReceiver = "The teleport request from <fromplayer> has expired.";

        @Comment("""
                Message to the receiver when the teleport request expires.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String noOpenTPARequest = "There is no open TPA request for <fromplayer>.";

        @Comment("""
                Message to the sender when the teleport request was accepted.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaAcceptSender = "Your TPA request to <toplayer> was accepted.";

        @Comment("""
                Message to the receiver when the teleport request was accepted.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaAcceptReceiver = "You accepted the TPA request from <fromplayer>.";

        @Comment("""
                Message to the sender when the teleport request was declined.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaDeclineSender = "Your TPA request to <toplayer> was declined.";

        @Comment("""
                Message to the receiver when the teleport request was declined.
                
                Allows for <fromplayer> (Name of the player that has sent the request)
                and <toplayer> (Name of the player that receives request)
                """)
        private String tpaDeclineReceiver = "You declined the TPA request from <fromplayer>.";

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

        /**
         * Retrieves the error message sent to a player if they want to teleport to themselves.
         *
         * @return the error message template
         */
        public String tpToSelf() {
            return this.tpToSelf;
        }

        /**
         * Sets the error message sent to a player if they want to teleport to themselves.
         *
         * @param tpToSelf the error message template
         */
        public void tpToSelf(final String tpToSelf) {
            this.tpToSelf = tpToSelf;
        }

        /**
         * Retrieves the error message sent to a player if they want to teleport another player to themselves.
         *
         * @return the error message template
         */
        public String tpOtherToThemselves() {
            return this.tpOtherToThemselves;
        }

        /**
         * Sets the error message sent to a player if they want to teleport another player to themselves.
         *
         * @param tpOtherToThemselves the error message template
         */
        public void tpOtherToThemselves(final String tpOtherToThemselves) {
            this.tpOtherToThemselves = tpOtherToThemselves;
        }

        /**
         * Retrieves the error message sent to a player if they try to send a teleport request to themselves.
         *
         * @return the error message template.
         */
        public String tpaToSelf() {
            return this.tpaToSelf;
        }

        /**
         * Sets the error message sent to a player if they try to send a teleport request to themselves.
         *
         * @param tpaToSelf the error message template.
         */
        public void tpaToSelf(final String tpaToSelf) {
            this.tpaToSelf = tpaToSelf;
        }

        /**
         * Retrieves the message sent to the sender when they send a TPA request.
         *
         * @return the TPA send message template.
         */
        public String tpaSendMessage() {
            return this.tpaSendMessage;
        }

        /**
         * Sets the message sent to the sender when they send a TPA request.
         *
         * @param tpaSendMessage the TPA send message template.
         */
        public void tpaSendMessage(final String tpaSendMessage) {
            this.tpaSendMessage = tpaSendMessage;
        }

        /**
         * Retrieves the message sent to the receiver when they receive a TPA request.
         *
         * @return the TPA receive message template.
         */
        public String tpaReceiveMessage() {
            return this.tpaReceiveMessage;
        }

        /**
         * Sets the message sent to the receiver when they receive a TPA request.
         *
         * @param tpaReceiveMessage the TPA receive message template.
         */
        public void tpaReceiveMessage(final String tpaReceiveMessage) {
            this.tpaReceiveMessage = tpaReceiveMessage;
        }

        /**
         * Retrieves the message sent to the sender when the TPA request expires.
         *
         * @return the TPA expire sender message template.
         */
        public String tpaExpireSender() {
            return this.tpaExpireSender;
        }

        /**
         * Sets the message sent to the sender when the TPA request expires.
         *
         * @param tpaTimeoutSender the TPA expire sender message template.
         */
        public void tpaExpireSender(final String tpaTimeoutSender) {
            this.tpaExpireSender = tpaTimeoutSender;
        }

        /**
         * Retrieves the message sent to the receiver when the TPA request expires.
         *
         * @return the TPA expire receiver message template.
         */
        public String tpaExpireReceiver() {
            return this.tpaExpireReceiver;
        }

        /**
         * Sets the message sent to the receiver when the TPA request expires.
         *
         * @param tpaTimeoutReceiver the TPA expire receiver message template.
         */
        public void tpaExpireReceiver(final String tpaTimeoutReceiver) {
            this.tpaExpireReceiver = tpaTimeoutReceiver;
        }

        /**
         * Retrieves the message sent to the player if no open TPA request is found.
         *
         * @return the no open TPA request message template.
         */
        public String noOpenTPARequest() {
            return this.noOpenTPARequest;
        }

        /**
         * Sets the message sent to the player if no open TPA request is found.
         *
         * @param noOpenTPARequest the no open TPA request message template.
         */
        public void noOpenTPARequest(final String noOpenTPARequest) {
            this.noOpenTPARequest = noOpenTPARequest;
        }

        /**
         * Retrieves the message sent to the sender when their TPA request is accepted.
         *
         * @return the TPA accept sender message template.
         */
        public String tpaAcceptSender() {
            return this.tpaAcceptSender;
        }

        /**
         * Sets the message sent to the sender when their TPA request is accepted.
         *
         * @param tpaAcceptSender the TPA accept sender message template.
         */
        public void tpaAcceptSender(final String tpaAcceptSender) {
            this.tpaAcceptSender = tpaAcceptSender;
        }

        /**
         * Retrieves the message sent to the receiver when they accept a TPA request.
         *
         * @return the TPA accept receiver message template.
         */
        public String tpaAcceptReceiver() {
            return this.tpaAcceptReceiver;
        }

        /**
         * Sets the message sent to the receiver when they accept a TPA request.
         *
         * @param tpaAcceptReceiver the TPA accept receiver message template.
         */
        public void tpaAcceptReceiver(final String tpaAcceptReceiver) {
            this.tpaAcceptReceiver = tpaAcceptReceiver;
        }

        /**
         * Retrieves the message sent to the sender when their TPA request is declined.
         *
         * @return the TPA decline sender message template.
         */
        public String tpaDeclineSender() {
            return this.tpaDeclineSender;
        }

        /**
         * Sets the message sent to the sender when their TPA request is declined.
         *
         * @param tpaDeclineSender the TPA decline sender message template.
         */
        public void tpaDeclineSender(final String tpaDeclineSender) {
            this.tpaDeclineSender = tpaDeclineSender;
        }

        /**
         * Retrieves the message sent to the receiver when they decline a TPA request.
         *
         * @return the TPA decline receiver message template.
         */
        public String tpaDeclineReceiver() {
            return this.tpaDeclineReceiver;
        }

        /**
         * Sets the message sent to the receiver when they decline a TPA request.
         *
         * @param tpaDeclineReceiver the TPA decline receiver message template.
         */
        public void tpaDeclineReceiver(final String tpaDeclineReceiver) {
            this.tpaDeclineReceiver = tpaDeclineReceiver;
        }
    }
}
