package com.ebicep.warlords.commands.debugcommands;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.commands.BaseCommand;
import com.ebicep.warlords.maps.Game;
import com.ebicep.warlords.maps.state.TimerDebugAble;
import com.ebicep.warlords.menu.DebugMenu;
import com.ebicep.warlords.player.WarlordsPlayer;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("warlords.game.debug")) {
            sender.sendMessage("§cYou do not have permission to do that.");
            return true;
        }

        Game game = Warlords.game; // In the future allow the user to select a game player
        if (args.length < 1) {
            DebugMenu.openDebugMenu((Player) sender);
            //sender.sendMessage("§cYou need to pass an argument, valid arguments: [timer, energy, cooldown, cooldownmode, takedamage]");
            return true;
        }
        String input = args[0];
        WarlordsPlayer player = BaseCommand.requireWarlordsPlayer(sender);
        if (input.equalsIgnoreCase("energy") ||
                input.equalsIgnoreCase("cooldown") ||
                input.equalsIgnoreCase("damage") ||
                input.equalsIgnoreCase("takedamage") ||
                input.equalsIgnoreCase("heal") ||
                input.equalsIgnoreCase("crits") ||
                input.equalsIgnoreCase("freeze") ||
                input.equalsIgnoreCase("timer")
        ) {
            if (args.length == 3 && args[2] != null) {
                player = Warlords.getPlayer(Bukkit.getPlayer(args[2]).getUniqueId());
            }
            if (player == null) { // We only have a warlords player if the game is running
                return true;
            }
        }

        if(input.equalsIgnoreCase("respawn")) {
            if (args.length == 2 && args[1] != null) {
                player = Warlords.getPlayer(Bukkit.getPlayer(args[1]).getUniqueId());
            }
            if (player == null) { // We only have a warlords player if the game is running
                return true;
            }
        }

        switch (input) {
            case "timer":
                if (!(game.getState() instanceof TimerDebugAble)) {
                    sender.sendMessage("§cThis gamestate cannot be manipulated by the timer debug option");
                    return true;
                }
                TimerDebugAble timerDebugAble = (TimerDebugAble) game.getState();
                if (args.length < 2) {
                    sender.sendMessage("§cTimer requires 2 or more arguments, valid arguments: [skip, reset]");
                    return true;
                }
                switch (args[1]) {
                    case "reset":
                        timerDebugAble.resetTimer();
                        sender.sendMessage(ChatColor.RED + "DEV: §aTimer has been reset!");
                        return true;
                    case "skip":
                        timerDebugAble.skipTimer();
                        sender.sendMessage(ChatColor.RED + "DEV: §aTimer has been skipped!");
                        return true;
                    default:
                        sender.sendMessage("§cInvalid option! [reset, skip]");
                        return true;
                }
            case "energy": {
                if (args.length < 2) {
                    sender.sendMessage("§cEnergy requires 2 or more arguments, valid arguments: [disable, enable]");
                    return true;
                }
                switch (args[1]) {
                    case "disable":
                        player.setInfiniteEnergy(true);
                        sender.sendMessage(ChatColor.RED + "DEV: " + player.getColoredName() + "'s §aEnergy consumption has been disabled!");
                        return true;
                    case "enable":
                        player.setInfiniteEnergy(false);
                        sender.sendMessage(ChatColor.RED + "DEV: " + player.getColoredName() + "'s §aEnergy consumption has been enabled!");
                        return true;
                    default:
                        sender.sendMessage("§cInvalid option!");
                        return false;
                }
            }

            case "cooldown": {
                if (args.length < 2) {
                    sender.sendMessage("§cCooldown requires 2 or more arguments, valid arguments: [disable, enable]");
                    return true;
                }
                switch (args[1]) {
                    case "disable":
                        player.setDisableCooldowns(true);
                        sender.sendMessage(ChatColor.RED + "DEV: " + player.getColoredName() + "'s §aCooldown timers have been disabled!");
                        return true;
                    case "enable":
                        player.setDisableCooldowns(false);
                        sender.sendMessage(ChatColor.RED + "DEV: " + player.getColoredName() + "'s §aCooldown timers have been enabled!");
                        return true;
                    default:
                        sender.sendMessage("§cInvalid option!");
                        return false;
                }
            }

            case "damage": {
                if (args.length < 2) {
                    sender.sendMessage("§cDamage requires 2 or more arguments, valid arguments: [disable, enable]");
                    return true;
                }
                switch (args[1]) {
                    case "disable":
                        player.setTakeDamage(false);
                        sender.sendMessage(ChatColor.RED + "§cDEV: " + player.getColoredName() + "'s §aTaking damage has been disabled!");
                        return true;
                    case "enable":
                        player.setTakeDamage(true);
                        sender.sendMessage(ChatColor.RED + "§cDEV: " + player.getColoredName() + "'s §aTaking damage has been enabled!");
                        return true;
                    default:
                        sender.sendMessage("§cInvalid option!");
                        return false;
                }
            }

            case "heal":
            case "takedamage": {
                if (args.length < 2) {
                    sender.sendMessage("§c" + (input.equals("takedamage") ? "Take Damage" : "Heal") + " requires more arguments, valid arguments: [1000, 2000, 3000, 4000, 5000]");
                    return true;
                }
                if (NumberUtils.isNumber(args[1])) {
                    int amount = Integer.parseInt(args[1]);

                    if (amount > 5000 || amount % 1000 != 0) {
                        sender.sendMessage("§cInvalid option! [Options: 1000, 2000, 3000, 4000, 5000]");
                        return false;
                    }

                    String endMessage = input.equals("takedamage") ? "took " + amount + " damage!" : "got " + amount + " heath!";
                    sender.sendMessage(ChatColor.RED + "§cDEV: " + player.getColoredName() + " §a" + endMessage);

                    if (input.equals("takedamage")) {
                        player.damageHealth(player, "debug", amount, amount, -1, 100, false);
                    } else {
                        player.healHealth(player, "debug", amount, amount, -1, 100, false);
                    }

                    return true;
                }
                sender.sendMessage("§cInvalid option! [Options: 1000, 2000, 3000, 4000, 5000]");
                return false;
            }

            case "cooldownmode": {
                if (args.length < 2) {
                    sender.sendMessage("§cCooldown Mode requires 2 or more arguments, valid arguments: [disable, enable]");
                    return true;
                }

                switch (args[1]) {
                    case "disable":
                        sender.sendMessage(ChatColor.RED + "Cooldown Mode can't be disabled in game!");
                        return true;
                    case "enable":
                        game.setCooldownMode(true);
                        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "-------------------------------");
                        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Cooldown Mode has been enabled by §c§l" + sender.getName() + "§6!");
                        Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "+50% Cooldown Reduction");
                        Bukkit.broadcastMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "-50% Energy Costs");
                        Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "+50% Max Health");
                        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "-------------------------------");
                        return true;
                    default:
                        sender.sendMessage("§cInvalid option!");
                        return false;
                }
            }

            case "crits":
                if (args.length < 2) {
                    sender.sendMessage("§cCrits requires 2 or more arguments, valid arguments: [disable, enable]");
                    return true;
                }

                switch (args[1]) {
                    case "disable":
                        player.setCanCrit(false);
                        sender.sendMessage(ChatColor.RED + "§cDEV: " + player.getColoredName() + "'s §aCrits has been disabled!");
                        return true;
                    case "enable":
                        player.setCanCrit(true);
                        sender.sendMessage(ChatColor.RED + "§cDEV: " + player.getColoredName() + "'s §aCrits has been enabled!");
                        return true;
                    default:
                        sender.sendMessage("§cInvalid option!");
                        return false;
                }

            case "respawn":
                player.respawn();
                return true;

            case "freeze": {
                if (player != null) {
                    player.getGame().freeze("", true);
                }
            }

            default:
                sender.sendMessage("§cInvalid option! valid args: [cooldownmode, cooldown, energy, damage, takedamage, freeze, timer");
                return true;
        }
    }

    public void register(Warlords instance) {
        instance.getCommand("wl").setExecutor(this);
        //instance.getCommand("wl").setTabCompleter(this);
    }
}
