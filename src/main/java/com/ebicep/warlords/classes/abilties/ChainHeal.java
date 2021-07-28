package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.classes.internal.AbstractChainBase;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.skilltree.SkillTree;
import com.ebicep.warlords.util.PlayerFilter;
import com.ebicep.warlords.util.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ChainHeal extends AbstractChainBase {

    public ChainHeal() {
        super("Chain Heal", 499, 674, 7.99f, 40, 20, 175);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Discharge a beam of energizing lightning\n" +
                "§7that heals you and a targeted friendly\n" +
                "§7player for §a" + minDamageHeal + " §7- §a" + maxDamageHeal + " §7health and\n" +
                "§7jumps to §e2 §7additional targets within\n" +
                "§e10 §7blocks. Each jump reduces the healing\n" +
                "§7by §c8%§7." +
                "\n\n" +
                "§7Each ally healed reduces the cooldown of\n" +
                "§7Boulder by §62 §7seconds.";
    }

    @Override
    public void createSkillTreeAbility(WarlordsPlayer warlordsPlayer, SkillTree skillTree) {

    }

    @Override
    protected int getHitCounterAndActivate(WarlordsPlayer wp, Player player) {
        int hitCounter = 0;
        for (WarlordsPlayer nearPlayer : PlayerFilter
                .entitiesAround(player, 15, 14, 15)
                .aliveTeammatesOfExcludingSelf(wp)) {
            if (Utils.isLookingAtChain(player, nearPlayer.getEntity()) && Utils.hasLineOfSight(player, nearPlayer.getEntity())) {
                //self heal
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                wp.addHealth(wp, name, minDamageHeal, maxDamageHeal, critChance, critMultiplier);
                chain(player.getLocation(), nearPlayer.getLocation());
                nearPlayer.addHealth(wp, name, minDamageHeal, maxDamageHeal, critChance, critMultiplier);
                hitCounter++;

                for (WarlordsPlayer chainPlayerOne : PlayerFilter
                        .entitiesAround(nearPlayer, 10, 9, 10)
                        .aliveTeammatesOf(wp)
                        .excluding(wp, nearPlayer)
                ) {
                    chain(nearPlayer.getLocation(), chainPlayerOne.getLocation());
                    chainPlayerOne.addHealth(wp, name, minDamageHeal * 0.92f, maxDamageHeal * 0.92f, critChance, critMultiplier);
                    hitCounter++;

                    for (WarlordsPlayer chainPlayerTwo : PlayerFilter
                            .entitiesAround(chainPlayerOne, 10, 9, 10)
                            .aliveTeammatesOf(wp)
                            .excluding(wp, nearPlayer, chainPlayerOne)
                    ) {
                        chain(chainPlayerOne.getLocation(), chainPlayerTwo.getLocation());
                        chainPlayerTwo.addHealth(wp, name, minDamageHeal * 0.84f, maxDamageHeal * 0.84f, critChance, critMultiplier);
                        hitCounter++;
                        break;
                    }
                    break;
                }
                break;
            }
        }
        return hitCounter;
    }

    @Override
    protected void onHit(WarlordsPlayer warlordsPlayer, Player player, int hitCounter) {
        if ((hitCounter + 1) * 2 > warlordsPlayer.getSpec().getRed().getCurrentCooldown()) {
            warlordsPlayer.getSpec().getRed().setCurrentCooldown(0);
        } else {
            warlordsPlayer.getSpec().getRed().setCurrentCooldown(warlordsPlayer.getSpec().getRed().getCurrentCooldown() - (hitCounter + 1) * 2);
        }
        warlordsPlayer.updateRedItem(player);
        warlordsPlayer.getSpec().getBlue().setCurrentCooldown((float) (cooldown * warlordsPlayer.getCooldownModifier()));

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), "shaman.chainheal.activation", 2, 1);
        }
        warlordsPlayer.updateBlueItem(player);
    }

    @Override
    protected ItemStack getChainItem() {
        return new ItemStack(Material.RED_ROSE, 1, (short) 1);
    }
}