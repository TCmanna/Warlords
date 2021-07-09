package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Berserk extends AbstractAbility {

    public Berserk() {
        super("Berserk", 0, 0, 46.98f, 30, 0, 0);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7You go into a berserker rage,\n" +
                "§7increasing your damage by §c25% §7and\n" +
                "§7movement speed by §e30%§7. While active,\n" +
                "§7you also take §c10% §7more damage.\n" + "§7Lasts §618 §7seconds.";
    }

    @Override
    public void onActivate(WarlordsPlayer wp, Player player) {
        wp.getSpeed().addSpeedModifier("Berserk", 30, 18 * 20, "BASE");
        wp.subtractEnergy(energyCost);
        wp.getCooldownManager().addCooldown(Berserk.this.getClass(), new Berserk(), "BERS", 18, wp, CooldownTypes.BUFF);

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), "warrior.berserk.activation", 2, 1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!wp.getCooldownManager().getCooldown(Berserk.class).isEmpty()) {
                    Location location = player.getLocation();
                    location.add(0, 2.1, 0);
                    ParticleEffect.VILLAGER_ANGRY.display(0, 0, 0, 0.1F, 1, location, 500);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Warlords.getInstance(), 0, 2);
    }

    @Override
    public void openMenu(Player player) {

    }
}
