package com.ebicep.warlords.abilties;

import com.ebicep.warlords.abilties.internal.AbstractStrikeBase;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.util.warlords.PlayerFilter;
import com.ebicep.warlords.util.warlords.Utils;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Comparator;

public class CrusadersStrike extends AbstractStrikeBase {

    private final int energyGiven = 30;
    private final int energyRadius = 25;

    public CrusadersStrike() {
        super("Crusader's Strike", 326, 441, 0, 90, 25, 175);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Strike the targeted enemy player,\n" +
                "§7causing §c" + format(minDamageHeal) + " §7- §c" + format(maxDamageHeal) + " §7damage and\n" +
                "§7restoring §e" + energyGiven + " §7energy to two nearby\n" +
                "§7allies within §e" + energyRadius + " §7blocks.";
    }

    @Override
    protected void onHit(@Nonnull WarlordsPlayer wp, @Nonnull Player player, @Nonnull WarlordsPlayer nearPlayer) {
        if (standingOnConsecrate(player, nearPlayer)) {
            nearPlayer.addDamageInstance(wp, name, minDamageHeal * 1.15f, maxDamageHeal * 1.15f, critChance, critMultiplier, false);
        } else {
            nearPlayer.addDamageInstance(wp, name, minDamageHeal, maxDamageHeal, critChance, critMultiplier, false);
        }
        //reloops near players to give energy to
        PlayerFilter.entitiesAround(wp, energyRadius, energyRadius, energyRadius)
                .aliveTeammatesOfExcludingSelf(wp)
                .sorted(
                    Comparator.comparing(
                        (WarlordsPlayer p) -> p.getCooldownManager().hasCooldown(HolyRadianceCrusader.class) ? 0 : 1)
                        .thenComparing(Utils.sortClosestBy(WarlordsPlayer::getLocation, wp.getLocation())
                    ))
                .limit(2)
                .forEach((nearTeamPlayer) -> {
                    nearTeamPlayer.addEnergy(wp, name, energyGiven);
                });
    }
}
