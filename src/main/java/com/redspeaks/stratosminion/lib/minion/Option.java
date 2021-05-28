package com.redspeaks.stratosminion.lib.minion;

import com.redspeaks.stratosminion.lib.chat.ChatUtil;

public class Option {

    private final String name;
    private final Equipment equipment;
    private final int startingPrice;
    public Option(String name, Equipment equipment, int startingPrice) {
        this.name = ChatUtil.colorize(name);
        this.equipment = equipment;
        this.startingPrice = startingPrice;
    }

    public String getName() {
        return name;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getStartingPrice() {
        return startingPrice;
    }
}
