package com.ebicep.warlords.player;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.maps.flags.GroundFlagLocation;
import com.ebicep.warlords.maps.flags.PlayerFlagLocation;
import com.ebicep.warlords.maps.flags.SpawnFlagLocation;
import com.ebicep.warlords.maps.state.PlayingState;
import com.ebicep.warlords.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomScoreboard {
    private final WarlordsPlayer warlordsPlayer;
    private final Scoreboard scoreboard;
    private final Objective sideBar;
    private static final String[] teamEntries = new String[]{"🎂", "🎉", "🎁", "👹", "🏀", "⚽", "🍭", "🌠", "👾", "🐍", "🔮", "👽", "💣", "🍫", "🔫"};
    private Objective health;
    private final PlayingState gameState;

    public CustomScoreboard(WarlordsPlayer warlordsPlayer, PlayingState gameState, int numberOfEntries) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();

        sideBar = scoreboard.registerNewObjective("WARLORDS", "dummy");
        sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sideBar.setDisplayName("§e§lWARLORDS");

        for (int i = 0; i < numberOfEntries; i++) {
            Team tempTeam = scoreboard.registerNewTeam("team_" + (i + 1));
            tempTeam.addEntry(teamEntries[i]);
            switch (i + 1) {
                case 1:
                    tempTeam.setPrefix(ChatColor.YELLOW + Warlords.VERSION);
                    break;
                case 5:
                    if (gameState != null) {
                        tempTeam.setPrefix(ChatColor.WHITE + "Class: ");
                        tempTeam.setSuffix(ChatColor.GREEN + warlordsPlayer.getSpec().getClass().getSimpleName());
                    }
                    break;
                default:
                    if (i == numberOfEntries - 1) {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        SimpleDateFormat format2 = new SimpleDateFormat("kk:mm");
                        tempTeam.setPrefix(ChatColor.GRAY + format.format(new Date()) + " - ");
                        tempTeam.setSuffix(format2.format(new Date()));
                    }
                    break;
            }
            sideBar.getScore(teamEntries[i]).setScore(i + 1);
        }

        this.gameState = gameState;
        this.warlordsPlayer = warlordsPlayer;
    }

    public void updateHealth() {
        if (health == null) {
            health = scoreboard.registerNewObjective("health", "dummy");
            health.setDisplaySlot(DisplaySlot.BELOW_NAME);
            health.setDisplayName(ChatColor.RED + "❤");
        }
        this.gameState.getGame().forEachOfflinePlayer((player, team) -> {
            WarlordsPlayer warlordsPlayer = Warlords.getPlayer(player);
            if (warlordsPlayer != null) {
                health.getScore(warlordsPlayer.getName()).setScore(warlordsPlayer.getHealth());
            }
        });
    }

    public void updateNames() {
        this.gameState.getGame().forEachOfflinePlayer((player, team) -> {
            WarlordsPlayer warlordsPlayer = Warlords.getPlayer(player);
            if (warlordsPlayer != null) {
                if (scoreboard.getTeam(warlordsPlayer.getName()) == null) {
                    Team temp = scoreboard.registerNewTeam(warlordsPlayer.getName());
                    temp.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + warlordsPlayer.getSpec().getClassNameShort() + ChatColor.DARK_GRAY + "] " + team.teamColor());
                    temp.addEntry(warlordsPlayer.getName());
                    temp.setSuffix(ChatColor.DARK_GRAY + " [" + ChatColor.GOLD + "Lv90" + ChatColor.DARK_GRAY + "]");
                } else {
                    if (warlordsPlayer.getGameState().flags().hasFlag(warlordsPlayer)) {
                        scoreboard.getTeam(warlordsPlayer.getName()).setSuffix(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Lv90" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + "⚑");
                    } else {
                        scoreboard.getTeam(warlordsPlayer.getName()).setSuffix(ChatColor.DARK_GRAY + " [" + ChatColor.GOLD + "Lv90" + ChatColor.DARK_GRAY + "]");
                    }
                }
            }
        });
    }

    public void updateBasedOnGameState(PlayingState gameState) {

        this.updateHealth();
        this.updateNames();

        // Timer
        {
            int secondsRemaining = gameState.getTimer() / 20;
            int minute = secondsRemaining / 60;
            int second = secondsRemaining % 60;
            String timeLeft = "";
            if (minute < 10) {
                timeLeft += "0";
            }
            timeLeft += minute + ":";
            if (second < 10) {
                timeLeft += "0";
            }
            timeLeft += second;

            com.ebicep.warlords.maps.Team team = gameState.calculateWinnerByPoints();
            if (team != null) {
                scoreboard.getTeam("team_10").setPrefix(team.coloredPrefix() + ChatColor.GOLD + " Wins in:");
            } else {
                scoreboard.getTeam("team_10").setPrefix(ChatColor.WHITE + "Time Left:");
            }
            scoreboard.getTeam("team_10").setSuffix(" " + ChatColor.GREEN + timeLeft);
        }
        // Points
        {
            scoreboard.getTeam("team_13").setPrefix(ChatColor.BLUE + "BLU: ");
            scoreboard.getTeam("team_13").setSuffix(ChatColor.AQUA.toString() + gameState.getBluePoints() + ChatColor.GOLD + "/1000");
            scoreboard.getTeam("team_12").setPrefix(ChatColor.RED + "RED: ");
            scoreboard.getTeam("team_12").setSuffix(ChatColor.AQUA.toString() + gameState.getRedPoints() + ChatColor.GOLD + "/1000");
        }
        // Flags
        {
            if (gameState.flags().getRed().getFlag() instanceof SpawnFlagLocation) {
                scoreboard.getTeam("team_8").setPrefix(ChatColor.RED + "RED Flag: ");
                scoreboard.getTeam("team_8").setSuffix(ChatColor.GREEN + "Safe");
            } else if (gameState.flags().getRed().getFlag() instanceof PlayerFlagLocation) {
                PlayerFlagLocation flag = (PlayerFlagLocation) gameState.flags().getRed().getFlag();
                if (flag.getPickUpTicks() == 0) {
                    scoreboard.getTeam("team_8").setPrefix(ChatColor.RED + "RED Flag: ");
                    scoreboard.getTeam("team_8").setSuffix(ChatColor.RED + "Stolen!");
                } else {
                    scoreboard.getTeam("team_8").setPrefix(ChatColor.RED + "RED Flag: " + ChatColor.RED + "St");
                    scoreboard.getTeam("team_8").setSuffix("olen!" + ChatColor.YELLOW + " +" + flag.getComputedHumanMultiplier() + "§e%");
                }
            } else if (gameState.flags().getRed().getFlag() instanceof GroundFlagLocation) {
                GroundFlagLocation flag = (GroundFlagLocation) gameState.flags().getRed().getFlag();
                scoreboard.getTeam("team_8").setPrefix(ChatColor.RED + "RED Flag: ");
                scoreboard.getTeam("team_8").setSuffix(ChatColor.YELLOW + "Dropped! " + ChatColor.GRAY + flag.getDespawnTimerSeconds());
            } else {
                scoreboard.getTeam("team_8").setPrefix(ChatColor.RED + "RED Flag: ");
                scoreboard.getTeam("team_8").setSuffix(ChatColor.GRAY + "Respawning...");
            }

            if (gameState.flags().getBlue().getFlag() instanceof SpawnFlagLocation) {
                scoreboard.getTeam("team_7").setPrefix(ChatColor.BLUE + "BLU Flag: ");
                scoreboard.getTeam("team_7").setSuffix(ChatColor.GREEN + "Safe");
            } else if (gameState.flags().getBlue().getFlag() instanceof PlayerFlagLocation) {
                PlayerFlagLocation flag = (PlayerFlagLocation) gameState.flags().getBlue().getFlag();
                if (flag.getPickUpTicks() == 0) {
                    scoreboard.getTeam("team_7").setPrefix(ChatColor.BLUE + "BLU Flag: ");
                    scoreboard.getTeam("team_7").setSuffix(ChatColor.RED + "Stolen!");
                } else {
                    scoreboard.getTeam("team_7").setPrefix(ChatColor.BLUE + "BLU Flag: " + ChatColor.RED + "St");
                    scoreboard.getTeam("team_7").setSuffix("olen!" + ChatColor.YELLOW + " +" + flag.getComputedHumanMultiplier() + "§e%");
                }
            } else if (gameState.flags().getBlue().getFlag() instanceof GroundFlagLocation) {
                GroundFlagLocation flag = (GroundFlagLocation) gameState.flags().getBlue().getFlag();
                scoreboard.getTeam("team_7").setPrefix(ChatColor.BLUE + "BLU Flag: ");
                scoreboard.getTeam("team_7").setSuffix(ChatColor.YELLOW + "Dropped! " + ChatColor.GRAY + flag.getDespawnTimerSeconds());
            } else {
                scoreboard.getTeam("team_7").setPrefix(ChatColor.BLUE + "BLU Flag: ");
                scoreboard.getTeam("team_7").setSuffix(ChatColor.GRAY + "Respawning...");
            }
        }
        //POINTS
        {
            scoreboard.getTeam("team_4").setPrefix("Points: " + ChatColor.GREEN + (int) warlordsPlayer.getPoints());
        }
    }

    public void updateKillsAssists() {
        scoreboard.getTeam("team_3").setPrefix(ChatColor.GREEN.toString() + warlordsPlayer.getTotalKills() + ChatColor.RESET + " Kills ");
        scoreboard.getTeam("team_3").setSuffix(ChatColor.GREEN.toString() + warlordsPlayer.getTotalAssists() + ChatColor.RESET + " Assists");
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public static void giveMainLobbyScoreboard(Player player) {
        Scoreboard mainLobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective sideBar = mainLobbyScoreboard.registerNewObjective("WARLORDS", "");
        sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sideBar.setDisplayName("§e§lWARLORDS");
        sideBar.getScore("").setScore(15);
        sideBar.getScore("Kills: " + ChatColor.GREEN + Utils.addCommaAndRound(((Integer) Warlords.databaseManager.getPlayerInformation(player, "kills")))).setScore(14);
        sideBar.getScore("Assists: " + ChatColor.GREEN + Utils.addCommaAndRound(((Integer) Warlords.databaseManager.getPlayerInformation(player, "assists")))).setScore(13);
        sideBar.getScore("Deaths: " + ChatColor.GREEN + Utils.addCommaAndRound(((Integer) Warlords.databaseManager.getPlayerInformation(player, "deaths")))).setScore(12);
        sideBar.getScore(" ").setScore(11);
        sideBar.getScore("Wins: " + ChatColor.GREEN + Utils.addCommaAndRound(((Integer) Warlords.databaseManager.getPlayerInformation(player, "wins")))).setScore(10);
        sideBar.getScore("Losses: " + ChatColor.GREEN + Utils.addCommaAndRound(((Integer) Warlords.databaseManager.getPlayerInformation(player, "losses")))).setScore(9);
        sideBar.getScore("  ").setScore(8);
        sideBar.getScore("Damage: " + ChatColor.RED + Utils.addCommaAndRound(((Number) Warlords.databaseManager.getPlayerInformation(player, "damage")).doubleValue())).setScore(7);
        sideBar.getScore("Healing: " + ChatColor.DARK_GREEN + Utils.addCommaAndRound(((Number) Warlords.databaseManager.getPlayerInformation(player, "healing")).doubleValue())).setScore(6);
        sideBar.getScore("Absorbed: " + ChatColor.GOLD + Utils.addCommaAndRound(((Number) Warlords.databaseManager.getPlayerInformation(player, "absorbed")).doubleValue())).setScore(5);
        sideBar.getScore("   ").setScore(4);
        sideBar.getScore("   ").setScore(3);
        sideBar.getScore("    ").setScore(2);
        sideBar.getScore(ChatColor.YELLOW + Warlords.VERSION).setScore(1);
        player.setScoreboard(mainLobbyScoreboard);
    }
}