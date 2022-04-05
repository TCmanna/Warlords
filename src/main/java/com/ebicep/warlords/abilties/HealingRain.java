package com.ebicep.warlords.abilties;

import com.ebicep.warlords.abilties.internal.AbstractAbility;
import com.ebicep.warlords.effects.ParticleEffect;
import com.ebicep.warlords.effects.circle.AreaEffect;
import com.ebicep.warlords.effects.circle.CircleEffect;
import com.ebicep.warlords.effects.circle.CircumferenceEffect;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.player.cooldowns.CooldownTypes;
import com.ebicep.warlords.player.cooldowns.cooldowns.RegularCooldown;
import com.ebicep.warlords.util.warlords.GameRunnable;
import com.ebicep.warlords.util.warlords.PlayerFilter;
import com.ebicep.warlords.util.warlords.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;


public class HealingRain extends AbstractAbility {

    private int duration = 10;
    private int radius = 6;

    public HealingRain() {
        super("Healing Rain", 170, 230, 70.49f, 50, 15, 200);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Conjure rain at targeted\n" +
                "location that will restore §a" + format(minDamageHeal) + "\n" +
                "§7- §a" + format(maxDamageHeal) + " §7health every second\n" +
                "to allies. Lasts §6" + duration + " §7seconds.";
    }

    @Override
    public boolean onActivate(WarlordsPlayer wp, Player player) {
        if (player.getTargetBlock((Set<Material>) null, 25).getType() == Material.AIR) return false;
        wp.subtractEnergy(energyCost);
        wp.getSpec().getOrange().setCurrentCooldown((float) (cooldown * wp.getCooldownModifier()));

        Location location = player.getTargetBlock((Set<Material>) null, 25).getLocation().clone();

        RegularCooldown<HealingRain> healingRainCooldown = new RegularCooldown<>(
                name,
                "RAIN",
                HealingRain.class,
                new HealingRain(),
                wp,
                CooldownTypes.ABILITY,
                cooldownManager -> {
                },
                duration * 20
        );
        wp.getCooldownManager().addCooldown(healingRainCooldown);

        Utils.playGlobalSound(location, "mage.healingrain.impact", 2, 1);

        CircleEffect circleEffect = new CircleEffect(
                wp.getGame(),
                wp.getTeam(),
                location,
                radius,
                new CircumferenceEffect(ParticleEffect.VILLAGER_HAPPY, ParticleEffect.REDSTONE),
                new AreaEffect(5, ParticleEffect.CLOUD).particlesPerSurface(0.025),
                new AreaEffect(5, ParticleEffect.DRIP_WATER).particlesPerSurface(0.025)
        );

        BukkitTask task = wp.getGame().registerGameTask(circleEffect::playEffects, 0, 1);

        location.add(0, 1, 0);

        new GameRunnable(wp.getGame()) {
            int counter = 0;

            @Override
            public void run() {
                if (counter % 20 == 0) {
                    for (WarlordsPlayer teammateInRain : PlayerFilter
                            .entitiesAround(location, radius, radius, radius)
                            .aliveTeammatesOf(wp)
                    ) {
                        teammateInRain.addHealingInstance(
                                wp,
                                name,
                                minDamageHeal,
                                maxDamageHeal,
                                critChance,
                                critMultiplier,
                                false,
                                false);
                    }
                }
                counter++;

                if (!wp.getCooldownManager().hasCooldown(healingRainCooldown)) {
                    this.cancel();
                    task.cancel();
                }
            }

        }.runTaskTimer(0, 0);

        return true;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
