package com.redspeaks.stratosminion.lib.minion;

import com.redspeaks.stratosminion.StratosMinion;
import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import com.redspeaks.stratosminion.lib.gui.AbstractGUI;
import com.redspeaks.stratosminion.lib.gui.ItemBuilder;
import com.redspeaks.stratosminion.lib.storage.Storage;
import me.revils.enchants.api.PublicRevAPI;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Minion implements InventoryHolder {

    private final MinionType type;
    private final ArmorStand as;
    private int fuel = 1;
    private long timeStamp = 0;
    private final OfflinePlayer owner;
    private int efficiency = 1;
    private int fortune = 1;
    private long token = 1;
    private long fuelRemovalStamp = 0;
    public Minion(Location location, MinionType type, OfflinePlayer spawner) {
        this.owner = spawner;
        this.type = type;
        as = location.getWorld().spawn(location, ArmorStand.class);
        as.setSmall(true);
        as.setCustomName(type.getOption().getName());
        as.setCustomNameVisible(true);
        as.setVisible(true);
        as.setGravity(true);
        as.setArms(true);
        as.getEquipment().setChestplate(type.getOption().getEquipment().getChest());
        as.getEquipment().setItemInMainHand(type.getOption().getEquipment().getHand());
        as.getEquipment().setHelmet(type.getOption().getEquipment().getSkull());
        as.getEquipment().setLeggings(type.getOption().getEquipment().getLeggings());
        as.getEquipment().setBoots(type.getOption().getEquipment().getBoots());
        as.setBasePlate(false);
        setAttribute(MinionAttribute.EFFICIENCY, 1);
        setAttribute(MinionAttribute.FORTUNE, 1);
        MinionManager.minions.add(this);
    }

    public Minion(Location location, MinionType type, OfflinePlayer spawner,  int efficiency, int fortune) {
        this.owner = spawner;
        this.type = type;
        as = location.getWorld().spawn(location, ArmorStand.class);
        as.setSmall(true);
        as.setCustomName(type.getOption().getName());
        as.setCustomNameVisible(true);
        as.setVisible(true);
        as.setGravity(true);
        as.setArms(true);
        as.getEquipment().setChestplate(type.getOption().getEquipment().getChest());
        as.getEquipment().setItemInMainHand(type.getOption().getEquipment().getHand());
        as.getEquipment().setHelmet(type.getOption().getEquipment().getSkull());
        as.getEquipment().setLeggings(type.getOption().getEquipment().getLeggings());
        as.getEquipment().setBoots(type.getOption().getEquipment().getBoots());
        as.setBasePlate(false);
        setAttribute(MinionAttribute.EFFICIENCY, efficiency);
        setAttribute(MinionAttribute.FORTUNE, fortune);
        MinionManager.minions.add(this);
    }

    public Minion(Location location, MinionType type, OfflinePlayer spawner,  int efficiency, int fortune, int fuel) {
        this.owner = spawner;
        this.type = type;
        as = location.getWorld().spawn(location, ArmorStand.class);
        as.setSmall(true);
        as.setCustomName(type.getOption().getName());
        as.setCustomNameVisible(true);
        as.setVisible(true);
        as.setGravity(true);
        as.setArms(true);
        as.getEquipment().setChestplate(type.getOption().getEquipment().getChest());
        as.getEquipment().setItemInMainHand(type.getOption().getEquipment().getHand());
        as.getEquipment().setHelmet(type.getOption().getEquipment().getSkull());
        as.getEquipment().setLeggings(type.getOption().getEquipment().getLeggings());
        as.getEquipment().setBoots(type.getOption().getEquipment().getBoots());
        as.setBasePlate(false);
        this.fuel = fuel;
        setAttribute(MinionAttribute.EFFICIENCY, efficiency);
        setAttribute(MinionAttribute.FORTUNE, fortune);
        MinionManager.minions.add(this);
    }

    public void kill() {
        if(as.getEquipment() == null) return;
        as.getEquipment().clear();
        as.remove();
    }

    public MinionType getType() {
        return type;
    }

    public ArmorStand getArmorStand() {
        return as;
    }

    public void startMining() {
        as.setRightArmPose(new EulerAngle(4, 0, 0));
        setTimeStamp(3);
    }

    public Long getFuelRemovalStamp() {
        return fuelRemovalStamp;
    }

    public void setFuelRemovalStamp(int minutes) {
        fuelRemovalStamp = ((minutes * 60L) * 1000L) + System.currentTimeMillis();
    }

    public void setTrueRemovalStamp(Long trueValue) {
        fuelRemovalStamp = trueValue;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int seconds) {
        timeStamp = (seconds * 1000L) + System.currentTimeMillis();
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public Location getLocation() {
        return as.getLocation();
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Minion) {
            Minion minion = (Minion) object;
            return minion.getArmorStand().getUniqueId().equals(this.getArmorStand().getUniqueId());
        }
        return false;
    }

    public int getAttribute(MinionAttribute minionAttribute) {
        if(minionAttribute == MinionAttribute.EFFICIENCY) {
            return efficiency;
        }
        return fortune;
    }

    public void setAttribute(MinionAttribute minionAttribute, int value) {
        if(minionAttribute == MinionAttribute.EFFICIENCY) {
            efficiency = value;
            return;
        }
        fortune = value;
    }

    public ItemStack convertToItem() {
        return new ItemBuilder(getType().getOption().getName(),
                Arrays.asList("", "&7Attributes:", "  &7" + MinionAttribute.EFFICIENCY.getName() + ": &f" + getAttribute(MinionAttribute.EFFICIENCY)
                        , "  &7" + MinionAttribute.FORTUNE.getName() + ": &f" + getAttribute(MinionAttribute.FORTUNE), "  " +
                                "&7Fuel: &f" + getFuel()), getType().getConversionType()).build();
    }

    public int getFuel() {
        return fuel;
    }

    public boolean emptyFuel() {
        return getFuel() == 0;
    }

    public void setFuel(int fuel) {
        if(fuel < 0) {
            this.fuel = 0;
            getInventory().getViewers().forEach(p -> p.openInventory(getInventory()));
            return;
        }
        this.fuel = fuel;
        getInventory().getViewers().forEach(p -> p.openInventory(getInventory()));
    }

    public void claimTokens(Player claimer) {
        if(getToken() == 0) return;
        PublicRevAPI.setTokens(claimer, PublicRevAPI.getTokens(claimer) + getToken());
        claimer.sendMessage(ChatUtil.colorize("&dYou received " + getToken() + " tokens"));
        setToken(0);
        claimer.openInventory(getInventory());
    }

    public Location getRelativeLocation() {
        Vector direction = as.getLocation().getDirection();
        Location location = as.getLocation().clone();
        return location.add(direction);
    }

    public void saveData(Storage storage) {
        String prefix = getLocation().getWorld().getName() +
                (int) getLocation().getX() +
                (int) getLocation().getY() +
                (int) getLocation().getZ();
        storage.set(prefix + ".location", getLocation());
        storage.set(prefix + ".type", getType().getName());
        storage.set(prefix + ".fuel", fuel);
        storage.set(prefix + ".attributes.efficiency", getAttribute(MinionAttribute.EFFICIENCY));
        storage.set(prefix + ".attributes.fortune", getAttribute(MinionAttribute.FORTUNE));
        storage.set(prefix + ".spawner", getOwner().getUniqueId().toString());
        storage.set(prefix + ".tokens", token);
        storage.set(prefix + ".fuel_stamp", getFuelRemovalStamp());
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public void deleteData() {
        String prefix = getLocation().getWorld().getName() +
                (int) getLocation().getX() +
                (int) getLocation().getY() +
                (int) getLocation().getZ();
        Storage storage = StratosMinion.getInstance().getStorage();
        storage.set(prefix, null);
        if(getRelativeLocation().getBlock().getType() != Material.AIR) {
            getRelativeLocation().getBlock().setType(Material.AIR);
        }
    }

    public AbstractGUI getGUI() {
        return new AbstractGUI("(" + getType().getName() + ")" + " &eMinion Menu", 6) {
            @Override
            public void init() {
                ItemBuilder star_dust = new ItemBuilder("&e&lStar Dust", Arrays.asList("", "&8➥ &7This is the fuel for the minion",
                        "&8➥ &7Right Click to add Select Amount of Fuel",
                        "&8➥ &7Left Click to add 1 Fuel at a Time",
                        "&8➥ &7Current fuel: &f" + getFuel()), Material.YELLOW_DYE);
                ItemBuilder tokens = new ItemBuilder("&d&lTokens: " + getToken(), Arrays.asList("", "&8➥ &7Left Click to claim your tokens!"), Material.PURPLE_DYE);
                ItemBuilder fortune = new ItemBuilder("&c&lFortune For Minion", Arrays.asList("", "&8➥ &7This Line Upgrades the Fortune of your minion", "&8➥ &7(Increase the amount of tokens you earn)", "&8➥ &7Cost: " + ((getAttribute(MinionAttribute.FORTUNE) + 1) * (getStartingCost(MinionAttribute.FORTUNE))) + " beacons", "&8➥ &7Next Level: " + (getAttribute(MinionAttribute.FORTUNE) + 1)), Material.REDSTONE);
                ItemBuilder efficiency = new ItemBuilder("&b&lEfficiency For Minion", Arrays.asList("", "&8➥ &7This Line Upgrades the Efficiency of your minion", "&8➥ &7(Earns tokens faster)", "&8➥ &7Cost: " + ((getAttribute(MinionAttribute.EFFICIENCY) + 1) * (getStartingCost(MinionAttribute.EFFICIENCY))) + " beacons", "&8➥ &7Next Level: " + (getAttribute(MinionAttribute.EFFICIENCY) + 1)), Material.QUARTZ);

                setItem(11, star_dust);
                setItem(17, tokens);
                setItem(37, fortune);
                setItem(46, efficiency);
                for (int i = 0; i < getAttribute(MinionAttribute.FORTUNE); i++) {
                    getInventory().setItem(attributeArray()[0][i], new ItemBuilder(" ", Collections.emptyList(), Material.GREEN_STAINED_GLASS_PANE).build());
                }
                for (int i = getAttribute(MinionAttribute.FORTUNE); i < attributeArray()[0].length; i++) {
                    getInventory().setItem(attributeArray()[0][i], new ItemBuilder(" ", Collections.emptyList(), Material.RED_STAINED_GLASS_PANE).build());
                }
                for (int i = 0; i < getAttribute(MinionAttribute.EFFICIENCY); i++) {
                    getInventory().setItem(attributeArray()[1][i], new ItemBuilder(" ", Collections.emptyList(), Material.GREEN_STAINED_GLASS_PANE).build());
                }
                for (int i = getAttribute(MinionAttribute.EFFICIENCY); i < attributeArray()[1].length; i++) {
                    getInventory().setItem(attributeArray()[1][i], new ItemBuilder(" ", Collections.emptyList(), Material.RED_STAINED_GLASS_PANE).build());
                }
                addEvent(e -> {
                    if(e.getCurrentItem().isSimilar(star_dust.build())) {
                        if(hasStardust((Player) e.getWhoClicked())) {
                            removeStardust((Player) e.getWhoClicked());
                            setFuel(getFuel() + 1);
                            e.setCancelled(true);
                            return;
                        }
                        e.getWhoClicked().sendMessage(ChatUtil.colorize("&cYou don't have any fuel"));
                        e.setCancelled(true);
                        return;
                    }
                    if(e.getCurrentItem().isSimilar(tokens.build())) {
                        claimTokens((Player) e.getWhoClicked());
                        e.setCancelled(true);
                        return;
                    }
                    if(e.getCurrentItem().isSimilar(fortune.build())) {
                        if(hasBeacon((Player) e.getWhoClicked(), ((getAttribute(MinionAttribute.FORTUNE) + 1) * (getStartingCost(MinionAttribute.FORTUNE))))) {
                            if(levelUp(MinionAttribute.FORTUNE)) {
                                for (int i = 0; i < getAttribute(MinionAttribute.FORTUNE); i++) {
                                    e.getClickedInventory().setItem(attributeArray()[0][i], new ItemBuilder(" ", Collections.emptyList(), Material.GREEN_STAINED_GLASS_PANE).build());
                                }
                                for (int i = getAttribute(MinionAttribute.FORTUNE); i < attributeArray()[0].length; i++) {
                                    e.getClickedInventory().setItem(attributeArray()[0][i], new ItemBuilder(" ", Collections.emptyList(), Material.RED_STAINED_GLASS_PANE).build());
                                }
                                removeBeacon((Player) e.getWhoClicked(), ((getAttribute(MinionAttribute.FORTUNE) + 1) * (getStartingCost(MinionAttribute.FORTUNE))));
                                e.getWhoClicked().openInventory(getInventory());
                            }

                            e.setCancelled(true);
                            return;
                        }
                        e.getWhoClicked().sendMessage(ChatUtil.colorize("&cYou don't have enough beacons"));
                        e.setCancelled(true);
                        return;
                    }
                    if(e.getCurrentItem().isSimilar(efficiency.build())) {
                        if(hasBeacon((Player) e.getWhoClicked(), ((getAttribute(MinionAttribute.EFFICIENCY) + 1) * (getStartingCost(MinionAttribute.EFFICIENCY))))) {
                            if (levelUp(MinionAttribute.EFFICIENCY)) {
                                for (int i = 0; i < getAttribute(MinionAttribute.EFFICIENCY); i++) {
                                    e.getClickedInventory().setItem(attributeArray()[1][i], new ItemBuilder(" ", Collections.emptyList(), Material.GREEN_STAINED_GLASS_PANE).build());
                                }
                                for (int i = getAttribute(MinionAttribute.EFFICIENCY); i < attributeArray()[1].length; i++) {
                                    e.getClickedInventory().setItem(attributeArray()[1][i], new ItemBuilder(" ", Collections.emptyList(), Material.RED_STAINED_GLASS_PANE).build());
                                }
                                removeBeacon((Player) e.getWhoClicked(), ((getAttribute(MinionAttribute.EFFICIENCY) + 1) * (getStartingCost(MinionAttribute.EFFICIENCY))));
                                e.getWhoClicked().openInventory(getInventory());
                            }
                            e.setCancelled(true);
                            return;
                        }
                        e.getWhoClicked().sendMessage(ChatUtil.colorize("&cYou don't have enough beacons"));
                        e.setCancelled(true);
                        return;
                    }
                    e.setCancelled(true);
                });
            }
        };
    }

    private int[][] attributeArray() {
        return new int[][] {{37,38,39,40,41,42,43,44}, {46,47,48,49,50,51,52,53}};
    }

    private boolean levelUp(MinionAttribute minionAttribute) {
        if(getAttribute(minionAttribute) == 8) {
            return false;
        }
        setAttribute(minionAttribute, getAttribute(minionAttribute) + 1);
        return true;
    }

    private int getStartingCost(MinionAttribute attribute) {
        if(attribute == MinionAttribute.EFFICIENCY) {
            return StratosMinion.getInstance().getSettings().getInt("starting-costs.efficiency");
        }
        return StratosMinion.getInstance().getSettings().getInt("starting-costs.fortune");
    }

    private boolean hasStardust(Player player) {
        int stardust = (int)Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(i -> i.getType() == Material.YELLOW_DYE).filter(i -> i.hasItemMeta()).filter(i -> i.getItemMeta().hasDisplayName()).filter(i -> ChatUtil.strip(i.getItemMeta().getDisplayName()).equalsIgnoreCase("Star Dust")).count();
        return stardust > 0;
    }

    private boolean hasBeacon(Player player, int requirements) {
        int beacons = 0;
        for(ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack == null) continue;
            if(itemStack.getType() == Material.BEACON) {
                beacons += itemStack.getAmount();
            }
        }
        return beacons >= requirements;
    }

    private void removeBeacon(Player player, int beacons) {
        ItemStack itemStack = new ItemStack(Material.BEACON, beacons);
        player.getInventory().removeItem(itemStack);
        player.updateInventory();
    }

    public void removeStardust(Player player) {
        for(ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack == null) continue;
            if(itemStack.getType() != Material.YELLOW_DYE) continue;
            if(!itemStack.hasItemMeta()) continue;
            if(!itemStack.getItemMeta().hasDisplayName()) continue;
            if(!ChatUtil.strip(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase("Star Dust")) continue;
            ItemStack clone = itemStack.clone();
            clone.setAmount(1);
            player.getInventory().removeItem(clone);
            break;
        }
        player.updateInventory();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        getGUI().init();
        return getGUI().getInventory();
    }
}
