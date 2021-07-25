package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.classes.internal.AbstractStrikeBase;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.skilltree.SkillTree;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class WoundingStrikeDefender extends AbstractStrikeBase {

    public WoundingStrikeDefender() {
        super("Wounding Strike", -415.8f, -556.5f, 0, 100, 20, 200);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7Strike the targeted enemy player,\n" +
                "§7causing §c" + Math.floor(-minDamageHeal) + " §7- §c" + Math.floor(-maxDamageHeal) + " §7damage\n" +
                "§7and §cwounding §7them for §63 §7seconds.\n" +
                "§7A wounded player receives §c25% §7less\n" +
                "§7healing for the duration of the effect.";
    }

    @Override
    public void createSkillTreeAbility(WarlordsPlayer warlordsPlayer, SkillTree skillTree) {

    }

    @Override
    protected void onHit(@Nonnull WarlordsPlayer wp, @Nonnull Player player, @Nonnull WarlordsPlayer nearPlayer) {
        nearPlayer.addHealth(wp, name, minDamageHeal, maxDamageHeal, critChance, critMultiplier);
        if (!nearPlayer.getCooldownManager().hasCooldown(WoundingStrikeBerserker.class)) {
            nearPlayer.getCooldownManager().removeCooldown(WoundingStrikeDefender.class);
            nearPlayer.getCooldownManager().addCooldown(this.getClass(), new WoundingStrikeDefender(), "WND", 3, wp, CooldownTypes.DEBUFF);
        }
        nearPlayer.sendMessage(ChatColor.GRAY + "You are " + ChatColor.RED + "wounded" + ChatColor.GRAY + ".");
    }
}