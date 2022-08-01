package me.nes0x.life.config;

public enum ConfigOption {
    LANGUAGE("language"),
    BAN_PERM("ban.perm"),
    BAN_DAYS("ban.days"),
    BAN_HOURS("ban.hours"),
    BAN_MINUTES("ban.minutes"),
    SETTINGS_STARTING_LIFE_NUMBER("settings.starting-life-number"),
    SETTINGS_REMOVE_LIFE_ON_DEATH_NUMBER("settings.remove-life-on-death-number"),
    SETTINGS_ENABLE_MESSAGE_ON_DEATH("settings.enable-message-on-death"),
    ADD_LIFE_ITEM_ENABLED("add-life-item.enabled"),
    ADD_LIFE_ITEM_ENABLE_MESSAGE_ON_CLICK("add-life-item.enable-message-on-click"),
    ADD_LIFE_ITEM_MATERIAL("add-life-item.material"),
    ADD_LIFE_ITEM_LIFE_TO_ADD("add-life-item.life-to-add"),
    ADD_LIFE_ITEM_NAME("add-life-item.name"),
    ADD_LIFE_ITEM_LORE("add-life-item.lore"),
    WORLD_GUARD_ENABLED("world-guard.enabled"),
    WORLD_GUARD_REGIONS("world-guard.regions"),
    VERSION("version");

    private final String path;

    ConfigOption(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
