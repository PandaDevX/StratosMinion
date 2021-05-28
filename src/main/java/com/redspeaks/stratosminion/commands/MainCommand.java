package com.redspeaks.stratosminion.commands;

import com.redspeaks.stratosminion.StratosMinion;
import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import com.redspeaks.stratosminion.lib.gui.ItemBuilder;
import com.redspeaks.stratosminion.lib.minion.Minion;
import com.redspeaks.stratosminion.lib.minion.MinionAttribute;
import com.redspeaks.stratosminion.lib.minion.MinionManager;
import com.redspeaks.stratosminion.lib.minion.MinionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("minion.admin")) {
            sendMessage(sender, "&cYou have no permission to do that");
            return true;
        }
        String subCommand = args[0];
        if(subCommand.equalsIgnoreCase("give")) {
            if(args.length < 3) {
                sendMessage(sender, "&7Correct usage: &f/minion give <type> <name> (efficiency) (fortune) (fuel)");
                sendMessage(sender, "&7() - &fOptional");
                sendMessage(sender, "&7<> - &fRequired");
                return true;
            }
            MinionType type = MinionType.getMinion(args[1]);
            if(type == MinionType.UNKNOWN) {
                sendMessage(sender, "&7Minion type does not exist");
                return true;
            }
            Player target = Bukkit.getPlayer(args[2]);
            if(target == null) {
                sendMessage(sender, "&7Player not found");
                return true;
            }
            if(args.length == 3) {
                ItemStack itemStack = new ItemBuilder(type.getOption().getName(),
                        Arrays.asList("", "&7Attributes:", "  &7" + MinionAttribute.EFFICIENCY.getName() + ": &f" + 1
                                , "  &7" + MinionAttribute.FORTUNE.getName() + ": &f" + 1, "  " +
                                        "&7Fuel: &f" + 1), type.getConversionType()).build();
                target.getInventory().addItem(itemStack);
                target.updateInventory();
                return true;
            }
            if(args.length == 4) {
                if(!ChatUtil.isInt(args[3])) {
                    sendMessage(sender, "&7Efficiency must be a valid number");
                    return true;
                }
                ItemStack itemStack = new ItemBuilder(type.getOption().getName(),
                        Arrays.asList("", "&7Attributes:", "  &7" + MinionAttribute.EFFICIENCY.getName() + ": &f" + ChatUtil.convertToInt(args[3])
                                , "  &7" + MinionAttribute.FORTUNE.getName() + ": &f" + 1, "  " +
                                        "&7Fuel: &f" + 1), type.getConversionType()).build();
                target.getInventory().addItem(itemStack);
                target.updateInventory();
                return true;
            }
            if(args.length == 5) {
                if(!ChatUtil.isInt(args[3])) {
                    sendMessage(sender, "&7Efficiency must be a valid number");
                    return true;
                }
                if(!ChatUtil.isInt(args[4])) {
                    sendMessage(sender, "&7Fortune must be a valid number");
                    return true;
                }
                ItemStack itemStack = new ItemBuilder(type.getOption().getName(),
                        Arrays.asList("", "&7Attributes:", "  &7" + MinionAttribute.EFFICIENCY.getName() + ": &f" + ChatUtil.convertToInt(args[3])
                                , "  &7" + MinionAttribute.FORTUNE.getName() + ": &f" + ChatUtil.convertToInt(args[4]), "  " +
                                        "&7Fuel: &f" + 1), type.getConversionType()).build();
                target.getInventory().addItem(itemStack);
                target.updateInventory();
                return true;
            }
            if(!ChatUtil.isInt(args[3])) {
                sendMessage(sender, "&7Efficiency must be a valid number");
                return true;
            }
            if(!ChatUtil.isInt(args[4])) {
                sendMessage(sender, "&7Fortune must be a valid number");
                return true;
            }
            if(!ChatUtil.isInt(args[5])) {
                sendMessage(sender, "&7Fuel must be a valid number");
                return true;
            }
            ItemStack itemStack = new ItemBuilder(type.getOption().getName(),
                    Arrays.asList("", "&7Attributes:", "  &7" + MinionAttribute.EFFICIENCY.getName() + ": &f" + ChatUtil.convertToInt(args[3])
                            , "  &7" + MinionAttribute.FORTUNE.getName() + ": &f" + ChatUtil.convertToInt(args[4]), "  " +
                                    "&7Fuel: &f" + ChatUtil.convertToInt(args[5])), type.getConversionType()).build();
            target.getInventory().addItem(itemStack);
            target.updateInventory();
            return true;
        }
        if(subCommand.equalsIgnoreCase("clear")) {
            MinionManager.getMinions().stream().filter(Objects::nonNull).forEach(m -> m.saveData(StratosMinion.getInstance().getStorage()));
            MinionManager.getMinions().stream().filter(Objects::nonNull).forEach(Minion::kill);
            MinionManager.getMinions().stream().filter(Objects::nonNull).forEach(Minion::deleteData);
            MinionManager.getMinions().clear();
            sendMessage(sender, "&dSuccessfully cleared minions");
            return true;
        }
        sendMessage(sender, "&7Unknown command");
        return false;
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatUtil.colorize(message));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            if(args[0].isEmpty()) {
                return Arrays.asList("clear", "give");
            }
            return Arrays.asList("clear", "give").stream().filter(i -> i.startsWith(args[0])).collect(Collectors.toList());
        }
        if(args.length == 2) {
            if(args[1].isEmpty()) {
                return Arrays.stream(MinionType.values()).filter(t -> t != MinionType.UNKNOWN).map(MinionType::getName).collect(Collectors.toList());
            }
            return Arrays.stream(MinionType.values()).filter(t -> t!= MinionType.UNKNOWN).map(MinionType::getName).filter(i -> i.startsWith(args[1])).collect(Collectors.toList());
        }
        return null;
    }
}
