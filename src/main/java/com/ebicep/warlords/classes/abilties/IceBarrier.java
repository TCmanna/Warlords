package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IceBarrier extends AbstractAbility {

    public IceBarrier() {
        super("Ice Barrier", 0, 0, 46.98f, 0, 0, 0
        );
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Surround yourself with a layer of\n" +
                "§7of cold air, reducing damage taken by\n" +
                "§c50%§7, While active, taking melee\n" +
                "§7damage reduces the attacker's movement\n" +
                "§7speed by §e20% §7for §62 §7seconds. Lasts\n" +
                "§66 §7seconds.";
    }

    @Override
    public void onActivate(WarlordsPlayer wp, Player player) {
        wp.getCooldownManager().addCooldown(IceBarrier.this.getClass(), new IceBarrier(), "ICE", 6, wp, CooldownTypes.ABILITY);

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), "mage.icebarrier.activation", 2, 1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!wp.getCooldownManager().getCooldown(IceBarrier.class).isEmpty()) {
                    Location location = player.getLocation();
                    location.add(0, 1.5, 0);
                    ParticleEffect.CLOUD.display(0.2F, 0.2F, 0.2F, 0.001F, 1, location, 500);
                    ParticleEffect.FIREWORKS_SPARK.display(0.3F, 0.2F, 0.3F, 0.0001F, 1, location, 500);
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
