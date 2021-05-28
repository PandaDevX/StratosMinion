package com.redspeaks.stratosminion.lib.minion;

import java.util.HashMap;

public enum MinionAttribute {
    FORTUNE("fortune", "Fortune"),
    EFFICIENCY("efficiency", "Efficiency");

    private String id;
    private String name;
    MinionAttribute(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
