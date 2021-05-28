package com.redspeaks.stratosminion.lib.gui;

import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LeatherBuilder {

    private String displayName = null;
    private List<String> lore = Collections.emptyList();
    private final Color color;
    private final Material type;
    private Enchantment[] enchantment = null;

    public LeatherBuilder(String displayName, List<String> lore, Color color, Material type, Enchantment... enchantment) {
        this.displayName = displayName;
        this.lore = lore;
        this.color = color;
        this.enchantment = enchantment;
        this.type = type;
    }

    public LeatherBuilder(String displayName, List<String> lore, Color color, Material type) {
        this.displayName = displayName;
        this.lore = lore;
        this.color = color;
        this.type = type;
    }

    public LeatherBuilder(Color color, Material type) {
        this.color = color;
        this.type = type;
    }

    public LeatherBuilder(String color, boolean rgb, Material type) {
        if(rgb) {
            this.color = Color.fromRGB(Integer.parseInt(color.split(", ")[0]), Integer.parseInt(color.split(", ")[1]), Integer.parseInt(color.split(", ")[2]));
        } else {
            this.color = DyeColor.valueOf(color).getColor();
        }
        this.type = type;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(type);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setColor(color);
        if(displayName != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        }
        if(!lore.isEmpty()) {
            itemMeta.setLore(ChatUtil.colorizeList(lore));
        }
        if(enchantment != null) {
            Arrays.stream(enchantment).forEach(e -> itemMeta.addEnchant(e, 1, true));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
