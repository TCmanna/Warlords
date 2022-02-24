package com.ebicep.warlords.database.repositories.player.pojos.general;

import com.ebicep.warlords.database.repositories.games.pojos.DatabaseGame;
import com.ebicep.warlords.database.repositories.games.pojos.DatabaseGamePlayers;
import com.ebicep.warlords.database.repositories.player.pojos.AbstractDatabaseStatInformation;
import com.ebicep.warlords.database.repositories.player.pojos.DatabasePlayer;
import com.ebicep.warlords.database.repositories.player.pojos.ctf.DatabasePlayerCTF;
import com.ebicep.warlords.database.repositories.player.pojos.general.classescomppub.*;
import com.ebicep.warlords.game.MapCategory;
import com.ebicep.warlords.player.Classes;
import com.ebicep.warlords.player.ClassesGroup;
import org.bukkit.GameMode;
import org.springframework.data.mongodb.core.mapping.Field;

public class DatabasePlayerPubStats extends AbstractDatabaseStatInformation implements DatabasePlayer {

    private DatabaseMage mage = new DatabaseMage();
    private DatabaseWarrior warrior = new DatabaseWarrior();
    private DatabasePaladin paladin = new DatabasePaladin();
    private DatabaseShaman shaman = new DatabaseShaman();
    private DatabaseRogue rogue = new DatabaseRogue();
    @Field("ctf_stats")
    private DatabasePlayerCTF ctfStats = new DatabasePlayerCTF();

    public DatabasePlayerPubStats() {
    }

    @Override
    public void updateCustomStats(MapCategory mapCategory, boolean isCompGame, DatabaseGame databaseGame, DatabaseGamePlayers.GamePlayer gamePlayer, boolean won, boolean add) {
        //UPDATE UNIVERSAL EXPERIENCE
        this.experience += add ? gamePlayer.getExperienceEarnedUniversal() : -gamePlayer.getExperienceEarnedUniversal();
        //UPDATE CLASS, SPEC
        this.getClass(Classes.getClassesGroup(gamePlayer.getSpec())).updateStats(mapCategory, isCompGame, databaseGame, gamePlayer, won, add);
        this.getSpec(gamePlayer.getSpec()).updateStats(mapCategory, isCompGame, databaseGame, gamePlayer, won, add);
        if (mapCategory == MapCategory.CAPTURE_THE_FLAG) {
            this.ctfStats.updateStats(mapCategory, isCompGame, databaseGame, gamePlayer, won, add);
        }
    }

    @Override
    public AbstractDatabaseStatInformation getSpec(Classes classes) {
        switch (classes) {
            case PYROMANCER:
                return mage.getPyromancer();
            case CRYOMANCER:
                return mage.getCryomancer();
            case AQUAMANCER:
                return mage.getAquamancer();
            case BERSERKER:
                return warrior.getBerserker();
            case DEFENDER:
                return warrior.getDefender();
            case REVENANT:
                return warrior.getRevenant();
            case AVENGER:
                return paladin.getAvenger();
            case CRUSADER:
                return paladin.getCrusader();
            case PROTECTOR:
                return paladin.getProtector();
            case THUNDERLORD:
                return shaman.getThunderlord();
            case SPIRITGUARD:
                return shaman.getSpiritguard();
            case EARTHWARDEN:
                return shaman.getEarthwarden();
            case ASSASSIN:
                return rogue.getAssassin();
            case VINDICATOR:
                return rogue.getVindicator();
            case APOTHECARY:
                return rogue.getApothecary();
        }
        return null;
    }

    @Override
    public AbstractDatabaseStatInformation getClass(ClassesGroup classesGroup) {
        switch (classesGroup) {
            case MAGE:
                return mage;
            case WARRIOR:
                return warrior;
            case PALADIN:
                return paladin;
            case SHAMAN:
                return shaman;
            case ROGUE:
                return rogue;
        }
        return null;
    }

    @Override
    public AbstractDatabaseStatInformation[] getClasses() {
        return new AbstractDatabaseStatInformation[]{mage, warrior, paladin, shaman, rogue};
    }

    public DatabaseMage getMage() {
        return mage;
    }

    public void setMage(DatabaseMage mage) {
        this.mage = mage;
    }

    public DatabaseWarrior getWarrior() {
        return warrior;
    }

    public void setWarrior(DatabaseWarrior warrior) {
        this.warrior = warrior;
    }

    public DatabasePaladin getPaladin() {
        return paladin;
    }

    public void setPaladin(DatabasePaladin paladin) {
        this.paladin = paladin;
    }

    public DatabaseShaman getShaman() {
        return shaman;
    }

    public void setShaman(DatabaseShaman shaman) {
        this.shaman = shaman;
    }

    public DatabaseRogue getRogue() {
        return rogue;
    }

    public void setRogue(DatabaseRogue rogue) {
        this.rogue = rogue;
    }

    public DatabasePlayerCTF getCtfStats() {
        return ctfStats;
    }

    public void setCtfStats(DatabasePlayerCTF ctfStats) {
        this.ctfStats = ctfStats;
    }
}
