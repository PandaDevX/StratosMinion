package com.redspeaks.stratosminion.lib.minion;

import com.redspeaks.stratosminion.StratosMinion;
import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import com.redspeaks.stratosminion.lib.gui.ItemBuilder;
import com.redspeaks.stratosminion.lib.gui.LeatherBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;

public enum MinionType {

    IRON("iron", "Iron", Material.IRON_BLOCK, Material.IRON_INGOT),
    COAL("coal", "Coal", Material.COAL_BLOCK, Material.COAL),
    GOLD("gold", "Gold", Material.GOLD_BLOCK, Material.GOLD_INGOT),
    DIAMOND("diamond", "Diamond", Material.DIAMOND_BLOCK, Material.DIAMOND),
    EMERALD("emerald", "Emerald", Material.EMERALD_BLOCK, Material.EMERALD),
    STRATOS_MINION("stratos", "Stratos", Material.MAGENTA_CONCRETE, Material.SHULKER_SHELL),
    UNKNOWN("unknown", "Unknown", Material.AIR, Material.AIR);

    private final String id;
    private final String name;
    private final Material type;
    private final Material conversionType;
    MinionType(String id, String name, Material type, Material conversionType) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.conversionType = conversionType;
    }

    public String getName() {
        return name;
    }

    public Material getConversionType() {
        return conversionType;
    }

    public Option getOption() {
        return new Option(getElement("name", String.class), new Equipment(getElement("equipment.head", String.class),
                getEquipment("chestplate"),
                new ItemBuilder(Material.getMaterial(getElement("equipment.hand", String.class))).build(),
                getEquipment("leggings"),
                getEquipment("boots")
        ), getElement("starting_price", Integer.class));
    }

    public <T> T getElement(String path, Class<T> type) {
        return type.cast(StratosMinion.getInstance().getSettings().get("Minion Type." + id + "." + path));
    }

    public ItemStack getEquipment(String type) {
        return new LeatherBuilder(getElement("equipment." + type + ".color", String.class),
                getElement("rgb", Boolean.class), Material.valueOf("LEATHER_" + type.toUpperCase())).build();
    }

    public Material getMaterial() {
        return type;
    }

    public String getId() {
        return id;
    }

    public static MinionType getMinion(String type) {
        return Arrays.stream(MinionType.values()).filter(t -> t.getId().equals(type.toLowerCase())).findAny().orElse(UNKNOWN);
    }

    public static MinionType parse(String text) {
        return getMinion(ChatUtil.strip(text).split(" ")[0].toLowerCase());
    }
}
