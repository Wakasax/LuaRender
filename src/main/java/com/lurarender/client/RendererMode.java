package com.lurarender.client;

public enum RendererMode {
    VANILLA("Vanilla"),
    ANGELICA("Angelica"),
    SODIUM("Sodium");

    private final String displayName;

    RendererMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RendererMode fromString(String value) {
        if (value == null) {
            return VANILLA;
        }

        for (RendererMode mode : values()) {
            if (mode.name().equalsIgnoreCase(value) || mode.displayName.equalsIgnoreCase(value)) {
                return mode;
            }
        }

        return VANILLA;
    }
}
