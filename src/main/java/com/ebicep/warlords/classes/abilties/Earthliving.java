package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Earthliving extends AbstractAbility {

    public Earthliving() {
        super("Earthliving Weapon", 0, 0, 15.66f, 30, 25, 240);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Imbue your weapon with the power of the\n" +
                "§7Earth, causing each of your melee attacks\n" +
                "§7to have a §e40% §7chance to heal you and §e2\n" +
                "§7nearby allies for §a240% §7weapon damage.\n" +
                "§7Lasts §68 §7seconds.\n" + "\n" +
                "§7The first hit is guaranteed to activate Earthliving.";
    }

    @Override
    public void onActivate(WarlordsPlayer wp, Player player) {
        wp.subtractEnergy(energyCost);
        wp.getCooldownManager().addCooldown(Earthliving.this.getClass(), new Earthliving(), "EARTH", 8, wp, CooldownTypes.ABILITY);

        wp.setFirstProc(true);

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), "shaman.earthlivingweapon.activation", 2, 1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!wp.getCooldownManager().getCooldown(Earthliving.class).isEmpty()) {
                    Location location = player.getLocation();
                    location.add(0, 1.2, 0);
                    ParticleEffect.VILLAGER_HAPPY.display(0.3F, 0.3F, 0.3F, 0.1F, 3, location, 500);
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


