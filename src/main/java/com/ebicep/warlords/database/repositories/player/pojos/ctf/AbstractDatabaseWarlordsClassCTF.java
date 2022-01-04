package com.ebicep.warlords.database.repositories.player.pojos.ctf;

import com.ebicep.warlords.database.repositories.player.pojos.AbstractDatabaseStatInformation;
import com.ebicep.warlords.database.repositories.player.pojos.DatabaseWarlordsClass;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class AbstractDatabaseWarlordsClassCTF extends AbstractDatabaseStatInformation implements DatabaseWarlordsClass {

    @Field("flags_captured")
    protected int flagsCaptured = 0;
    @Field("flags_returned")
    protected int flagsReturned = 0;

    public AbstractDatabaseWarlordsClassCTF() {
    }

//    public void updateStats(DatabaseGamePlayers.GamePlayer gamePlayer, boolean won, boolean add) {
//        int operation = add ? 1 : -1;
//        this.kills += gamePlayer.getTotalKills() * operation;
//        this.assists += gamePlayer.getTotalAssists() * operation;
//        this.deaths += gamePlayer.getTotalDeaths() * operation;
//        if (won) {
//            this.wins += operation;
//        } else {
//            this.losses += operation;
//        }
//        this.plays += operation;
//        this.flagsCaptured += gamePlayer.getFlagCaptures() * operation;
//        this.flagsReturned += gamePlayer.getFlagReturns() * operation;
//        this.damage += gamePlayer.getTotalDamage() * operation;
//        this.healing += gamePlayer.getTotalHealing() * operation;
//        this.absorbed += gamePlayer.getTotalAbsorbed() * operation;
//        this.experience += gamePlayer.getExperienceEarnedSpec() * operation;
//    }

    public int getFlagsCaptured() {
        return flagsCaptured;
    }

    public void setFlagsCaptured(int flagsCaptured) {
        this.flagsCaptured = flagsCaptured;
    }

    public int getFlagsReturned() {
        return flagsReturned;
    }

    public void setFlagsReturned(int flagsReturned) {
        this.flagsReturned = flagsReturned;
    }
}
