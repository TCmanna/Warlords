package com.ebicep.warlords.classes.abilties;

import com.ebicep.warlords.classes.AbstractAbility;
import com.ebicep.warlords.effects.circle.CircleEffect;
import com.ebicep.warlords.effects.circle.CircumferenceEffect;
import com.ebicep.warlords.player.CooldownTypes;
import com.ebicep.warlords.player.WarlordsPlayer;
import com.ebicep.warlords.skilltree.SkillTree;
import com.ebicep.warlords.util.ItemBuilder;
import com.ebicep.warlords.util.ParticleEffect;
import com.ebicep.warlords.util.PlayerFilter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;


public class UndyingArmy extends AbstractAbility {

    public static final ItemStack BONE = new ItemBuilder(Material.BONE)
            .name(ChatColor.RED + "Instant Kill")
            .lore("§7Right-click this item to die\n§7instantly instead of waiting for\n§7the decay.")
            .get();

    private HashMap<UUID, Boolean> playersPopped = new HashMap<>();

    public HashMap<UUID, Boolean> getPlayersPopped() {
        return playersPopped;
    }

    public boolean isArmyDead(UUID uuid) {
        return playersPopped.get(uuid);
    }

    public void pop(UUID uuid) {
        playersPopped.put(uuid, true);
    }

    public UndyingArmy() {
        super("Undying Army", 0, 0, 60f + 10.47f, 20, 0, 0);
    }

    @Override
    public void updateDescription(Player player) {
        description = "§7When you or nearby allies take\n" +
                "§7fatal damage within §610 §7seconds,\n" +
                "§7instantly restore them to §a100% §7health\n" +
                "§7instead. They will take §c500 §7TRUE DAMAGE\n" +
                "§7every second for the rest of their life.\n" +
                "§7Allies not revived will heal for §a200 §7+\n" +
                "§a35% §7of their missing health §610 §7seconds\n" +
                "§7after this abilty was cast.";
    }



    @Override
    public void onActivate(WarlordsPlayer wp, Player player) {
        wp.subtractEnergy(energyCost);
        UndyingArmy tempUndyingArmy = new UndyingArmy();
        tempUndyingArmy.getPlayersPopped().put(wp.getUuid(), false);
        wp.getCooldownManager().addCooldown(name, UndyingArmy.this.getClass(), tempUndyingArmy, "ARMY", 10, wp, CooldownTypes.ABILITY);

        Iterator<WarlordsPlayer> iterator = PlayerFilter.entitiesAround(wp, 6, 6, 6)
                .aliveTeammatesOfExcludingSelf(wp)
                .iterator();
        int numberOfPlayersWithArmy = 0;
        while (iterator.hasNext()) {
            WarlordsPlayer warlordsNearPlayer = iterator.next();
            tempUndyingArmy.getPlayersPopped().put(warlordsNearPlayer.getUuid(), false);
            wp.sendMessage("§a\u00BB§7 " + ChatColor.GRAY + "Your Undying Army is now protecting " + warlordsNearPlayer.getColoredName() + ChatColor.GRAY + ".");
            warlordsNearPlayer.getCooldownManager().addCooldown(name, UndyingArmy.this.getClass(), tempUndyingArmy, "ARMY", 10, wp, CooldownTypes.ABILITY);
            warlordsNearPlayer.sendMessage("§a\u00BB§7 " + ChatColor.GRAY + wp.getName() + "'s Undying Army protects you for " + ChatColor.GOLD + "10 " + ChatColor.GRAY + "seconds.");
            numberOfPlayersWithArmy++;
        }
        String allies = numberOfPlayersWithArmy == 1 ? "ally." : "allies.";
        wp.sendMessage("§a\u00BB§7 " + ChatColor.GRAY + "Your Undying Army is protecting " + ChatColor.YELLOW + numberOfPlayersWithArmy + ChatColor.GRAY + " nearby " + allies);

        for (Player player1 : player.getWorld().getPlayers()) {
            player1.playSound(player.getLocation(), Sound.ZOMBIE_IDLE, 1, 1.1f);
        }

        CircleEffect circle = new CircleEffect(wp.getGame(), wp.getTeam(), player.getLocation(), 6);
        circle.addEffect(new CircumferenceEffect(ParticleEffect.VILLAGER_HAPPY).particlesPerCircumference(1));
        circle.playEffects();
    }

    @Override
    public void createSkillTreeAbility(WarlordsPlayer warlordsPlayer, SkillTree skillTree) {

    }
}
