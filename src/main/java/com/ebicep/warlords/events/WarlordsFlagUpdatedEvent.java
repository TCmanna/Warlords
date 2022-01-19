package com.ebicep.warlords.events;

import com.ebicep.warlords.maps.Game;
import com.ebicep.warlords.maps.Team;
import com.ebicep.warlords.maps.flags.FlagInfo;
import com.ebicep.warlords.maps.flags.FlagLocation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarlordsFlagUpdatedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Game game;
    private final FlagInfo info;
    private final Team team;
    private final FlagLocation old;

    public WarlordsFlagUpdatedEvent(Game game, FlagInfo info, Team team, FlagLocation old) {
        this.game = game;
        this.info = info;
        this.team = team;
        this.old = old;
    }

    public Game getGame() {
        return game;
    }

    public FlagInfo getInfo() {
        return info;
    }

    public Team getTeam() {
        return team;
    }

    public FlagLocation getOld() {
        return old;
    }

    public FlagLocation getNew() {
        return info.getFlag();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public String toString() {
        return "WarlordsFlagUpdatedEvent{" + "game=" + game + ", info=" + info + ", team=" + team + ", old=" + old + '}';
    }
}