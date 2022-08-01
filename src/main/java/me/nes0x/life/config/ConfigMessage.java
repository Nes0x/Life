package me.nes0x.life.config;

public enum ConfigMessage {
    COMMAND_IN_CONSOLE("command-in-console"),
    COMMAND_NOT_EXISTS("command-not-exists"),
    NO_LIFE_PLACEHOLDER("no-life-placeholder"),
    UNKNOWN_PLAYER("unknown-player"),
    NUMBER_OF_LIFE("number-of-life"),
    NO_PERMISSION("no-permission"),
    ADD_LIFE_SUCCESS("add-life-success"),
    REMOVE_LIFE_SUCCESS("remove-life-success"),
    SET_LIFE_SUCCESS("set-life-success"),
    GIVE_LIFE_SUCCESS("give-life-success"),
    GIVE_LIFE_SUCCESS_TO_TARGET("give-life-success-to-target"),
    GIVE_LIFE_ERROR("give-life-error"),
    ITEM_ADD_LIFE_SUCCESS("item-add-life-success"),
    INVALID_USAGE("invalid-usage"),
    PERM_BAN_REASON("perm-ban-reason"),
    TEMP_BAN_REASON("temp-ban-reason"),
    RELOAD_MESSAGE("reload-message"),
    NUMBER_EXCEPTION("number-exception"),
    LIFE_NUMBER_EXCEPTION("life-number-exception"),
    SELF_GIVE_LIFE("self-give-life"),
    MESSAGE_ON_DEATH("message-on-death"),
    MESSAGE_ON_CLICK("message-on-click"),
    DAYS("days"),
    HOURS("hours"),
    MINUTES("minutes");

    private final String path;

    ConfigMessage(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
