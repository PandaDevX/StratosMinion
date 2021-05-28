package com.redspeaks.stratosminion.lib.minion;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class Equipment {

    private final ItemStack chest, hand, leggings, boots;
    private final String url;
    public Equipment(String url, ItemStack chest, ItemStack hand, ItemStack leggings, ItemStack boots) {
        this.url = url;
        this.chest = chest;
        this.hand = hand;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ItemStack getSkull() {
        ItemStack head= new ItemStack(Material.PLAYER_HEAD);

        if(url.length() <= 16) {
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(url));
            head.setItemMeta(skullMeta);
            return head;
        }

        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        if(url.startsWith("https://")) {
            byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        } else {
            profile.getProperties().put("textures", new Property("textures", url));
        }
        Field profileField = null;

        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        profileField.setAccessible(true);

        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

    public ItemStack getChest() {
        return chest;
    }

    public ItemStack getHand() {
        return hand;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }
}
