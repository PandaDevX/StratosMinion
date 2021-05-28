package com.redspeaks.stratosminion.lib.chat;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final ArmorStand baseArmorstand;
    private final List<ArmorStand> armorStands;
    public Hologram(Location baseLocation, String lineOne) {
        this.baseArmorstand = baseLocation.getWorld().spawn(baseLocation, ArmorStand.class);
        baseArmorstand.setInvulnerable(true);
        baseArmorstand.setInvisible(true);
        baseArmorstand.setCustomName(ChatUtil.colorize(lineOne));
        baseArmorstand.setCustomNameVisible(true);

        this.armorStands = new ArrayList<>();
        armorStands.add(baseArmorstand);
    }

    public Hologram(Location baseLocation, String... lines) {
        this.baseArmorstand = baseLocation.getWorld().spawn(baseLocation, ArmorStand.class);
        this.armorStands = new ArrayList<>();

        baseArmorstand.setInvulnerable(true);
        baseArmorstand.setInvisible(true);
        baseArmorstand.setCustomName(ChatUtil.colorize(lines[0]));
        baseArmorstand.setCustomNameVisible(true);

        for(int i = 0; i < lines.length; i++) {
            if(i == 0) continue;
            addLine(lines[i]);
        }
    }

    public Hologram(Location baseLocation, List<String> lines) {
        this.baseArmorstand = baseLocation.getWorld().spawn(baseLocation, ArmorStand.class);
        this.armorStands = new ArrayList<>();

        baseArmorstand.setInvulnerable(true);
        baseArmorstand.setInvisible(true);
        baseArmorstand.setCustomName(ChatUtil.colorize(lines.get(0)));
        baseArmorstand.setCustomNameVisible(true);

        for(int i = 0; i < lines.size(); i++) {
            if(i == 0) continue;
            addLine(lines.get(i));
        }
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public void addLine(String content) {
        Location location = armorStands.get(armorStands.size() - 1).getLocation().clone().add(0, 0.22, 0);
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(ChatUtil.colorize(content));
        armorStand.setInvisible(true);

        armorStands.add(armorStand);
    }

    public void removeLine(int line) {
        if(line <= 0) {
            line = 1;
        }
        if(line > armorStands.size()) {
            line = armorStands.size();
        }
        if(armorStands.size() == 1) {
            clear();
            return;
        }
        int index = line - 1;
        ArmorStand as = armorStands.get(index);
        if(as != null) {
            as.remove();
            for(int i = line; i < armorStands.size(); i++) {
                Location location = armorStands.get(i).getLocation().clone().subtract(0, 0.22, 0);
                armorStands.get(i).teleport(location);
                location = null;
            }
        }
    }

    public void moveTo(Location location) {
        for(int i = 0; i < armorStands.size(); i++) {
            Location clone = location.clone().add(0, (i * 0.22), 0);
            armorStands.get(i).teleport(clone);
            clone = null;
        }
    }

    public void editLine(int line, String content) {
        ArmorStand as = armorStands.get(line);
        as.setCustomName(ChatUtil.colorize(content));
    }

    public void clear() {
        armorStands.forEach(Entity::remove);
        armorStands.clear();
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Hologram)) {
            return false;
        }
        Hologram hologram = (Hologram) object;
        return hologram.getArmorStands().equals(getArmorStands());
    }


}
