package com.ebicep.warlords.party;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.queuesystem.QueueManager;
import com.ebicep.warlords.util.ChatUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class PartyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("warlords.party.interaction")) {
            sender.sendMessage(ChatColor.RED + "Only verified comp players can join the party!");
            return true;
        }

        switch (s) {
            case "party":
            case "p":
                Player player = (Player) sender;
                if (args.length > 0) {
                    Optional<Party> currentParty = Warlords.partyManager.getPartyFromAny(player.getUniqueId());
                    String input = args[0];
                    if (!input.equals("join") && !input.equals("invite") && Bukkit.getOnlinePlayers().stream().noneMatch(p -> p.getName().equalsIgnoreCase(input))) {
                        if (!currentParty.isPresent()) {
                            Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You are currently not in a party!", true, true);
                            return true;
                        }
                    }
                    switch (input) {
                        case "invite":
                            if (args.length > 1) {
                                if (!currentParty.isPresent()) {
                                    Party party = new Party(((Player) sender).getUniqueId(), false);
                                    Warlords.partyManager.getParties().add(party);
                                    currentParty = Optional.of(party);
                                }
                                if (!currentParty.get().isAllInvite() && !currentParty.get().getLeader().equals(player.getUniqueId()) && currentParty.get().getModerators().stream().noneMatch(uuid -> uuid.equals(player.getUniqueId()))) {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "All Invite is Disabled!", true, true);
                                    return true;
                                }
                                Player partyLeader = Bukkit.getPlayer(currentParty.get().getLeader());
                                String playerToInvite = args[1];
                                Player invitedPlayer = Bukkit.getPlayer(playerToInvite);
                                if (invitedPlayer != null) {
                                    if (!Warlords.partyManager.inSameParty(player.getUniqueId(), invitedPlayer.getUniqueId())) {
                                        if (currentParty.get().getInvites().containsKey(invitedPlayer.getUniqueId())) {
                                            Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "That player has already been invited! (" + currentParty.get().getInvites().get(invitedPlayer.getUniqueId()) + ")", true, true);
                                        } else {
                                            currentParty.get().invite(playerToInvite);
                                            currentParty.get().sendMessageToAllPartyPlayers(
                                                    ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " invited " + ChatColor.AQUA + invitedPlayer.getName() + ChatColor.YELLOW + " to the party!\n" +
                                                            ChatColor.YELLOW + "They have" + ChatColor.RED + " 60 " + ChatColor.YELLOW + "seconds to accept!",
                                                    true,
                                                    true
                                            );
                                            ChatUtils.sendCenteredMessage(invitedPlayer, ChatColor.BLUE.toString() + ChatColor.BOLD + "------------------------------------------");
                                            ChatUtils.sendCenteredMessage(invitedPlayer, ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has invited you to join " + (partyLeader.equals(player) ? "their party!" : ChatColor.AQUA + partyLeader.getName() + ChatColor.YELLOW + "'s party!"));
                                            TextComponent message = new TextComponent(ChatColor.YELLOW + "You have" + ChatColor.RED + " 60 " + ChatColor.YELLOW + "seconds to accept. " + ChatColor.GOLD + "Click here to join!");
                                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Click to join the party!").create()));
                                            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + partyLeader.getName()));
                                            ChatUtils.sendCenteredMessageWithEvents(invitedPlayer, Collections.singletonList(message));
                                            ChatUtils.sendCenteredMessage(invitedPlayer, ChatColor.BLUE.toString() + ChatColor.BOLD + "------------------------------------------");
                                        }
                                    } else {
                                        Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "That player is already in the party!", true, true);
                                    }
                                } else {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Unable to invite that player!", true, true);
                                }
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                            }
                            return true;
                        case "join":
                            if (args.length > 1) {
                                if (currentParty.isPresent()) {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You are already in a party", true, true);
                                    return true;
                                }
                                String playerWithParty = args[1];
                                if (Bukkit.getOnlinePlayers().stream().anyMatch(p -> p.getName().equalsIgnoreCase(playerWithParty))) {
                                    Player partyLeader = Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(playerWithParty)).findAny().get();
                                    Optional<Party> party = Warlords.partyManager.getPartyFromLeader(partyLeader.getUniqueId());
                                    if (party.isPresent()) {
                                        if (party.get().isOpen() || party.get().getInvites().containsKey(player.getUniqueId())) {
                                            party.get().join(player.getUniqueId());
                                            QueueManager.queue.remove(player.getUniqueId());
                                        } else {
                                            Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invite expired or party is closed!", true, true);
                                        }
                                    } else {
                                        Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "That player does not have a party!", true, true);
                                    }
                                } else {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Cannot find a player with that name!", true, true);
                                }
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                            }
                            return true;
                        case "leave":
                            currentParty.get().leave(player.getUniqueId());
                            Party.sendMessageToPlayer((Player) sender, ChatColor.GREEN + "You left the party", true, true);
                            return true;
                        case "disband":
                            if (currentParty.get().getLeader().equals(player.getUniqueId())) {
                                currentParty.get().disband();
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You are not the party leader!", true, true);
                            }
                            return true;
                        case "list":
                            sender.sendMessage(currentParty.get().getList());
                            return true;
                        case "promote":
                        case "demote":
                        case "kick":
                        case "remove":
                        case "transfer":
                            if (args.length > 1) {
                                String playerToActOn = args[1];
                                if (currentParty.get().getLeader().equals(player.getUniqueId()) ||
                                        (currentParty.get().getModerators().contains(player.getUniqueId())) && !currentParty.get().getLeaderName().equalsIgnoreCase(playerToActOn) && !input.equalsIgnoreCase("promote") && !input.equalsIgnoreCase("demote") && !input.equalsIgnoreCase("transfer")) {
                                    if (player.getName().equalsIgnoreCase(playerToActOn)) {
                                        Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You cannot do this on yourself!", true, true);
                                    } else {
                                        if (input.equalsIgnoreCase("promote")) {
                                            currentParty.get().promote(playerToActOn);
                                        } else if (input.equalsIgnoreCase("demote")) {
                                            currentParty.get().demote(playerToActOn);
                                        } else if (input.equalsIgnoreCase("remove") || input.equalsIgnoreCase("kick")) {
                                            currentParty.get().remove(playerToActOn);
                                        } else {
                                            currentParty.get().transfer(playerToActOn);
                                        }
                                    }
                                } else {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Insufficient Permissions!", true, true);
                                }
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                            }
                            return true;
                        case "poll":
                            if (args.length > 1) {
                                if (currentParty.get().getLeader().equals(player.getUniqueId())) {
                                    if (!currentParty.get().getPolls().isEmpty()) {
                                        Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "There is already an ongoing poll!", true, true);
                                        return true;
                                    }
                                    String pollInfo = args[1];
                                    int numberOfSlashes = (int) pollInfo.chars().filter(ch -> ch == '/').count();
                                    if (numberOfSlashes <= 1) {
                                        Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You must have a question and more than 1 answer!", true, true);
                                    } else {
                                        List<String> pollOptions = new ArrayList<>(Arrays.asList(pollInfo.split("/")));
                                        String question = pollOptions.get(0);
                                        pollOptions.remove(question);
                                        currentParty.get().addPoll(question, pollOptions);
                                    }
                                    return true;
                                } else {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You are not the party leader!", true, true);
                                }
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                            }
                            return true;
                        case "pollanswer":
                            if (args.length > 1) {
                                if (!currentParty.get().getPolls().isEmpty()) {
                                    try {
                                        int answer = Integer.parseInt(args[1]);
                                        Poll poll = currentParty.get().getPolls().get(0);
                                        if (poll.getPlayerAnsweredWithOption().containsKey(player.getUniqueId())) {
                                            Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "You already voted for this poll!", true, true);
                                        } else if (answer > 0 && answer <= poll.getOptions().size()) {
                                            poll.getPlayerAnsweredWithOption().put(player.getUniqueId(), answer);
                                            Party.sendMessageToPlayer((Player) sender, ChatColor.GREEN + "You voted for " + poll.getOptions().get(answer - 1) + "!", true, true);
                                        } else {
                                            Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                                        }
                                        return true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                                    }
                                } else {
                                    Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "There is no ongoing poll!", true, true);
                                }
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Invalid Arguments!", true, true);
                            }
                            return true;
                        case "pollend":
                            if (!currentParty.get().getPolls().isEmpty()) {
                                Poll poll = currentParty.get().getPolls().get(0);
                                poll.setTimeLeft(0);
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "There is no ongoing poll!", true, true);
                            }
                            return true;
                        case "afk":
                            currentParty.get().afk(player.getUniqueId());
                            return true;
                        case "close":
                        case "open": {
                            if (currentParty.get().getLeader().equals(((Player) sender).getUniqueId())) {
                                currentParty.get().setOpen(!input.equalsIgnoreCase("close"));
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Insufficient Permissions!", true, true);
                            }
                            return true;
                        }
                        case "outside": {
                            if (currentParty.isPresent()) {
                                StringBuilder outside = new StringBuilder(ChatColor.YELLOW + "Players Outside Party: ");
                                final int[] numberOfPlayersOutside = {0};
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (!currentParty.get().getMembers().containsKey(p.getUniqueId())) {
                                        numberOfPlayersOutside[0]++;
                                        outside.append(ChatColor.GREEN).append(p.getName()).append(ChatColor.GRAY).append(", ");
                                    }
                                }
                                outside.setLength(outside.length() - 2);
                                if (numberOfPlayersOutside[0] == 0) {
                                    sender.sendMessage(ChatColor.YELLOW + "There are no players outside of the party");
                                } else {
                                    sender.sendMessage(outside.toString());
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You are currently not in a party!");
                            }
                            return true;
                        }
                        case "leader": {
                            currentParty.ifPresent(party -> {
                                if (sender.hasPermission("warlords.party.forceleader")) {
                                    party.transfer(sender.getName());
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
                                }
                            });
                            return true;
                        }
                        case "forcejoin": {
                            currentParty.ifPresent(party -> {
                                if (sender.hasPermission("warlords.party.forcejoin")) {
                                    String name = args[0];
                                    if (name.equalsIgnoreCase("@a")) {
                                        Bukkit.getOnlinePlayers().stream()
                                                .filter(p -> !p.getName().equalsIgnoreCase(party.getLeaderName()))
                                                .forEach(p -> Bukkit.getServer().dispatchCommand(p, "p join " + party.getLeaderName()));
                                    } else {
                                        Bukkit.getOnlinePlayers().stream()
                                                .filter(p -> p.getName().equalsIgnoreCase(name))
                                                .forEach(p -> Bukkit.getServer().dispatchCommand(p, "p join " + party.getLeaderName()));
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
                                }
                            });
                            return true;
                        }
                        case "allinvite": {
                            if (currentParty.get().getLeader().equals(((Player) sender).getUniqueId())) {
                                currentParty.get().setAllInvite(!currentParty.get().isAllInvite());
                            } else {
                                Party.sendMessageToPlayer((Player) sender, ChatColor.RED + "Insufficient Permissions!", true, true);
                            }
                            return true;
                        }
                        case "invitequeue":
                            int partySize = currentParty.get().getMembers().size();
                            if (partySize != 24) {
                                int availableSpots = 24 - partySize;
                                int onlineQueueSize = (int) QueueManager.queue.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).count();
                                List<UUID> toInvite = new ArrayList<>();
                                int inviteNumber;
                                if (availableSpots % 2 == 0) { //even spots
                                    inviteNumber = Math.min(availableSpots, onlineQueueSize % 2 == 0 ? onlineQueueSize : onlineQueueSize - 1);
                                } else { //odd spots
                                    inviteNumber = Math.min(availableSpots, onlineQueueSize % 2 != 0 ? onlineQueueSize : onlineQueueSize - 1);
                                }
                                System.out.println(inviteNumber);
                                int counter = 0;
                                if (inviteNumber != 0) {
                                    for (UUID uuid : QueueManager.queue) {
                                        Player invitePlayer = Bukkit.getPlayer(uuid);
                                        if (invitePlayer != null) {
                                            toInvite.add(uuid);
                                            counter++;
                                            if (counter >= inviteNumber) {
                                                break;
                                            }
                                        }
                                    }
                                    toInvite.forEach(uuid -> {
                                        Bukkit.dispatchCommand(sender, "p invite " + Bukkit.getPlayer(uuid).getName());
                                    });
                                    QueueManager.queue.removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
                                    QueueManager.sendNewQueue();
                                }
                            }
                            return true;
                        default:
                            Bukkit.getServer().dispatchCommand(sender, "p invite " + input);
                            return true;
                    }
                } else {
                    Party.sendMessageToPlayer(player, ChatColor.GOLD + "Party Commands: \n" +
                                    ChatColor.YELLOW + "/p invite <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Invites another player to your party" + "\n" +
                                    ChatColor.YELLOW + "/p (l/list)" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Lists the players in your current party" + "\n" +
                                    ChatColor.YELLOW + "/p leave" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Leaves your current party" + "\n" +
                                    ChatColor.YELLOW + "/p disband" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Disbands the party" + "\n" +
                                    ChatColor.YELLOW + "/p (kick/remove) <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Removes a player from your party" + "\n" +
                                    ChatColor.YELLOW + "/p transfer <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Transfers ownership of the party to a player" + "\n" +
                                    ChatColor.YELLOW + "/p afk" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Toggles if you are AFK" + "\n" +
                                    ChatColor.YELLOW + "/p promote <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Promotes a player in the party" + "\n" +
                                    ChatColor.YELLOW + "/p demote <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Demotes a player in the party" + "\n" +
                                    ChatColor.YELLOW + "/p poll <question/answer/answer...>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Creates a poll to vote on" + "\n" +
                                    ChatColor.YELLOW + "/p (open/close)" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Opens/Closes the party" + "\n" +
                                    ChatColor.YELLOW + "/p outside" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Shows the players outside of the party" + "\n" +
                                    ChatColor.YELLOW + "/p leader" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Takes leader from current leader" + "\n" +
                                    ChatColor.YELLOW + "/p forcejoin" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Forces a player to join your party" + "\n" +
                                    ChatColor.YELLOW + "/p allinvite" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Toggles All Invite" + "\n" +
                                    ChatColor.YELLOW + "/p invitequeue" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + ChatColor.ITALIC + "Invites the players in queue" + "\n"
                            ,
                            true,
                            false);
                }
                return true;
        }
        return true;
    }

    public void register(Warlords instance) {
        instance.getCommand("party").setExecutor(this);
    }

}