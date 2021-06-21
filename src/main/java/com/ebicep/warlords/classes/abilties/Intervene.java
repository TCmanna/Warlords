package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.util.ParticleEffect;
import com.ebicep.warlords.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Intervene extends AbstractAbility {
    private float damagePrevented = 0;

    public Intervene() {
        super("Intervene", 0, 0, 14.09f, 20, 0, 0);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Protect the target ally, reducing\n" +
                "§7the damage they take by §e100%\n" +
                "§7and redirecting §e50% §7of the damage\n" +
                "§7they would have taken back to you.\n" +
                "§7You can protect the target for a maximum\n" +
                "§7of §c3600 §7damage. You must remain within\n" +
                "§e15 §7blocks of each other. Lasts §65 §7seconds.";
    }

    @Override
    public void onActivate(Player player) {
        WarlordsPlayer warlordsPlayer = Warlords.getPlayer(player);
        List<Entity> near = player.getNearbyEntities(10.0D, 10.0D, 10.0D);
        near = Utils.filterOnlyTeammates(near, player);

        setDamagePrevented(0);

        for (Entity entity : near) {
            if (entity instanceof Player) {
                Player nearPlayer = (Player) entity;
                if (nearPlayer.getGameMode() != GameMode.SPECTATOR && Utils.getLookingAt(player, nearPlayer)) {
                    //green line thingy
                    Location lineLocation = player.getLocation().add(0, 1, 0);
                    lineLocation.setDirection(lineLocation.toVector().subtract(entity.getLocation().add(0, 1, 0).toVector()).multiply(-1));
                    for (int i = 0; i < Math.floor(player.getLocation().distance(entity.getLocation())) * 4; i++) {
                        ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0.35F, 1, lineLocation, 500);
                        lineLocation.add(lineLocation.getDirection().multiply(.25));
                    }

                    WarlordsPlayer nearWarlordsPlayer = Warlords.getPlayer(nearPlayer);
                    warlordsPlayer.getPlayer().sendMessage("§a\u00BB§7 You are now protecting " + nearWarlordsPlayer.getName() + " with your §eIntervene!");
                    warlordsPlayer.getCooldownManager().addCooldown(Intervene.this.getClass(), "VENE", 6, warlordsPlayer, CooldownTypes.ABILITY);
                    nearWarlordsPlayer.getPlayer().sendMessage("§a\u00BB§7 " + warlordsPlayer.getName() + " is shielding you with their " + ChatColor.YELLOW + "Intervene" + ChatColor.GRAY + "!");
                    nearPlayer.removeMetadata("INTERVENE", Warlords.getInstance());
                    nearPlayer.setMetadata("INTERVENE", new FixedMetadataValue(Warlords.getInstance(), this));
                    nearWarlordsPlayer.getCooldownManager().getCooldowns().removeAll(nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.this.getClass()));
                    nearWarlordsPlayer.getCooldownManager().addCooldown(Intervene.this.getClass(), "VENE", 6, warlordsPlayer, CooldownTypes.ABILITY);

                    warlordsPlayer.getSpec().getBlue().setCurrentCooldown(cooldown);
                    warlordsPlayer.updateBlueItem();

                    for (Player player1 : player.getWorld().getPlayers()) {
                        player1.playSound(player.getLocation(), "warrior.intervene.impact", 1, 1);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).size() > 0) {
                                if (nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0).getTimeLeft() <= 1)
                                    nearWarlordsPlayer.getPlayer().sendMessage("§a\u00BB§7 " + warlordsPlayer.getName() + "'s §eIntervene §7will expire in §6" + (int) (nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0).getTimeLeft() + .5) + "§7 second!");
                                else
                                    nearWarlordsPlayer.getPlayer().sendMessage("§a\u00BB§7 " + warlordsPlayer.getName() + "'s §eIntervene §7will expire in §6" + (int) (nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0).getTimeLeft() + .5) + "§7 seconds!");
                            } else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Warlords.getInstance(), 0, 20);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).size() > 0) {
                                if (nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0).getFrom().isDead() ||
                                        nearWarlordsPlayer.getPlayer().getLocation().distanceSquared(nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0).getFrom().getPlayer().getLocation()) > 15 * 15
                                ) {
                                    nearWarlordsPlayer.getPlayer().sendMessage("§c\u00AB§7 " + nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0).getFrom().getName() + "'s " + ChatColor.YELLOW + "Intervene " + ChatColor.GRAY + "has expired!");
                                    nearWarlordsPlayer.getCooldownManager().getCooldowns().remove(nearWarlordsPlayer.getCooldownManager().getCooldown(Intervene.class).get(0));
                                    nearWarlordsPlayer.getPlayer().removeMetadata("INTERVENE", Warlords.getInstance());
                                }
                            }
                        }
                    }.runTaskTimer(Warlords.getInstance(), 0, 0);
                    break;
                }
            }
        }
    }

    public void setDamagePrevented(float damagePrevented) {
        this.damagePrevented = damagePrevented;
    }

    public float getDamagePrevented() {
        return damagePrevented;
    }

    public void addDamagePrevented(float amount) {
        this.damagePrevented += amount;
    }

}
