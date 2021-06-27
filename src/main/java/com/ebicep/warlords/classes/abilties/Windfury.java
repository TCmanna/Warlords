package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Windfury extends AbstractAbility {

    public Windfury() {
        super("Windfury Weapon", 0, 0, 15.66f, 30, 25, 135);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Imbue your weapon with the power\n" +
                "§7of the wind, causing each of your\n" +
                "§7melee attacks to have a §e35% §7chance\n" +
                "§7to hit §e2 §7additional times for §c135%\n" +
                "§7weapon damage. The first melee hit is\n" +
                "§7guaranteed to activate Windfury. Lasts §68\n" +
                "§7seconds.";
    }

    @Override
    public void onActivate(WarlordsPlayer warlordsPlayer, Player player) {
        warlordsPlayer.subtractEnergy(energyCost);
        warlordsPlayer.getCooldownManager().addCooldown(Windfury.this.getClass(), "FURY", 8, warlordsPlayer, CooldownTypes.ABILITY);

        warlordsPlayer.setFirstProc(true);

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), "shaman.windfuryweapon.activation", 2, 1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (warlordsPlayer.getCooldownManager().getCooldown(Windfury.class).size() > 0) {
                    Location location = player.getLocation();
                    location.add(0, 1.2, 0);
                    ParticleEffect.CRIT.display(0.2F, 0F, 0.2F, 0.1F, 3, location, 500);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Warlords.getInstance(), 0, 4);
    }
}
