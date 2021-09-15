package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.events.WarlordsEvents;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.skilltree.SkillTree;
import com.ebicep.warlords.util.ParticleEffect;
import com.ebicep.warlords.util.PlayerFilter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Boulder extends AbstractAbility {

    private static final double SPEED = 0.480;
    private static final double GRAVITY = -0.01075;

    public Boulder() {
        super("Boulder", -490, -731, 7.05f, 80, 15, 175);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Launch a giant boulder that shatters\n" +
                "§7and deals §c" + -minDamageHeal + " §7- §c" + -maxDamageHeal + " §7damage\n" +
                "§7to all enemies near the impact point\n" +
                "§7and knocks them back slightly.";
    }



    @Override
    public void onActivate(WarlordsPlayer wp, Player player) {
        wp.subtractEnergy(energyCost);

        Location location = player.getLocation();
        Vector speed = player.getLocation().getDirection().multiply(SPEED);
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setHelmet(new ItemStack(Material.LONG_GRASS, 1, (short) 2));
        stand.setCustomName("Boulder");
        stand.setCustomNameVisible(false);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setArms(false);
        stand.setVisible(false);

        new BukkitRunnable() {

            @Override
            public void run() {
                quarterStep(false);
                quarterStep(false);
                quarterStep(false);
                quarterStep(true);
            }

            private void quarterStep(boolean last) {

                if (!stand.isValid()) {
                    this.cancel();
                    return;
                }

                speed.add(new Vector(0, GRAVITY * SPEED, 0));
                Location newLoc = stand.getLocation();
                newLoc.add(speed);
                stand.teleport(newLoc);
                newLoc.add(0, 1.7, 0);

                stand.setHeadPose(new EulerAngle(-speed.getY() * 3, 0, 0));

                boolean shouldExplode;

                if (last) {
                    ParticleEffect.CRIT.display(0.3F, 0.3F, 0.3F, 0.1F, 4, newLoc.clone().add(0, -1, 0), 500);
                }

                WarlordsPlayer directHit = null;
                if (!newLoc.getBlock().isEmpty()) {
                    // Explode based on collision
                    shouldExplode = true;
                } else {
                    directHit = PlayerFilter
                            .entitiesAroundRectangle(newLoc, 1.25, 2.5, 1.25)
                            .aliveEnemiesOf(wp).findFirstOrNull();
                    shouldExplode = directHit != null;
                }


                if (shouldExplode) {
                    stand.remove();
                    for (Player player1 : player.getWorld().getPlayers()) {
                        player1.playSound(newLoc, "shaman.boulder.impact", 2, 1);
                    }

                    for (WarlordsPlayer p : PlayerFilter
                            .entitiesAround(newLoc, 5, 5, 5)
                            .aliveEnemiesOf(wp)
                    ) {
                        p.addHealth(wp, name, minDamageHeal, maxDamageHeal, critChance, critMultiplier, false);

                        Entity entity = p.getEntity();
                        Vector v;
                        if (p == directHit) {
                            v = player.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(-.95).setY(0.2);
                        } else {
                            v = entity.getLocation().toVector().subtract(newLoc.toVector()).normalize().multiply(.95).setY(0.2);
                        }
                        entity.setVelocity(v);

                    }
                    newLoc.setPitch(-12);
                    Location impactLocation = newLoc.clone().subtract(speed);

                    //ParticleEffect.VILLAGER_HAPPY.display(0 , 0 ,0, 0, 10, impactLocation, 1000);

                    spawnFallingBlocks(impactLocation, 3, 10);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            spawnFallingBlocks(impactLocation, 3.5, 20);
                        }
                    }.runTaskLater(Warlords.getInstance(), 1);
                    this.cancel();
                }
            }

        }.runTaskTimer(Warlords.getInstance(), 0, 1);

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), "shaman.boulder.activation", 2, 1);
        }
    }

    private void spawnFallingBlocks(Location impactLocation, double initialCircleRadius, int amount) {
        double angle = 0;

        for (int i = 0; i < amount; i++) {
            FallingBlock fallingBlock;
            Location spawnLoc = impactLocation.clone();

            double x = initialCircleRadius * Math.cos(angle);
            double z = initialCircleRadius * Math.sin(angle);
            angle += 360.0 / amount + (int) (Math.random() * 4 - 2);

            spawnLoc.add(x, 0, z);

            //ParticleEffect.VILLAGER_HAPPY.display(0 , 0 ,0, 0, 1, spawnLoc, 100);

            if(spawnLoc.getWorld().getBlockAt(spawnLoc).getType() == Material.AIR) {
                switch ((int) (Math.random() * 3)) {
                    case 0:
                        fallingBlock = impactLocation.getWorld().spawnFallingBlock(spawnLoc, Material.DIRT, (byte) 0);
                        break;
                    case 1:
                        fallingBlock = impactLocation.getWorld().spawnFallingBlock(spawnLoc, Material.STONE, (byte) 0);
                        break;
                    case 2:
                        fallingBlock = impactLocation.getWorld().spawnFallingBlock(spawnLoc, Material.DIRT, (byte) 2);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + (int) (Math.random() * 3));
                }
                fallingBlock.setVelocity(impactLocation.toVector().subtract(spawnLoc.toVector()).normalize().multiply(-.5).setY(.25));
                fallingBlock.setDropItem(false);
                WarlordsEvents.addEntityUUID(fallingBlock);
            }

        }
    }

    @Override
    public void createSkillTreeAbility(WarlordsPlayer warlordsPlayer, SkillTree skillTree) {

    }
}