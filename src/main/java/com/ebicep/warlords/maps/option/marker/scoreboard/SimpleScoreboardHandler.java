package com.ebicep.warlords.maps.option.marker.scoreboard;

import com.ebicep.warlords.player.WarlordsPlayer;


public abstract class SimpleScoreboardHandler extends AbstractScoreboardHandler {
    
    protected int priority;
    protected String group;

    public SimpleScoreboardHandler(int priority, String group) {
        this.priority = priority;
        this.group = group;
    }

    @Override
    public int getPriority(WarlordsPlayer player) {
        return priority;
    }

    @Override
    public String getGroup() {
        return group;
    }

}
