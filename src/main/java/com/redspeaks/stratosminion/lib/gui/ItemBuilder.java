package com.redspeaks.stratosminion.lib.gui;

import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemBuilder {

    private String displayName = null;
    private List<String> lore = Collections.emptyList();
    private final Material type;
    private Enchantment[] enchantment = null;

    public ItemBuilder(String displayName, List<String> lore, Material type, Enchantment... enchantment) {
        this.displayName = displayName;
        this.lore = lore;
        this.type = type;
        this.enchantment = enchantment;
    }

    public ItemBuilder(String displayName, List<String> lore, Material type) {
        this.displayName = displayName;
        this.lore = lore;
        this.type = type;
    }

    public ItemBuilder(Material type) {
        this.type = type;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
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
