package com.framework.enums;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");

    private final String browserName;

    Browser(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static Browser fromString(String name) {
        for (Browser b : values()) {
            if (b.browserName.equalsIgnoreCase(name)) return b;
        }
        throw new IllegalArgumentException("Unsupported browser: " + name);
    }
}
