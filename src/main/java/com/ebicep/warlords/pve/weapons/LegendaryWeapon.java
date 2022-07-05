package com.ebicep.warlords.pve.weapons;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.player.general.SkillBoosts;
import com.ebicep.warlords.player.general.Specializations;
import org.bukkit.ChatColor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LegendaryWeapon extends AbstractWeapon {

    protected String title;
    protected Specializations specialization;
    @Field("skill_boost")
    protected SkillBoosts selectedSkillBoost;
    @Field("unlocked_skill_boosts")
    protected List<SkillBoosts> unlockedSkillBoosts = new ArrayList<>();
    @Field("energy_per_second_bonus")
    protected int energyPerSecondBonus;
    @Field("energy_per_hit_bonus")
    protected int energyPerHitBonus;
    @Field("spped_second")
    protected int speedBonus;

    public LegendaryWeapon() {

    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Energy per Second: " + ChatColor.GREEN + "+" + energyPerSecondBonus + "%",
                ChatColor.GRAY + "Energy per Hit: " + ChatColor.GREEN + "+" + energyPerHitBonus + "%",
                ChatColor.GRAY + "Speed: " + ChatColor.GREEN + "+" + speedBonus + "%",
                "",
                ChatColor.GREEN + Specializations.getClass(specialization).name + " (" + specialization.name + "):",
                ChatColor.GRAY + selectedSkillBoost.name + " - Description placeholder"
        );
    }

    public LegendaryWeapon(UUID uuid) {
        Specializations selectedSpec = Warlords.getPlayerSettings(uuid).getSelectedSpec();
        List<SkillBoosts> skillBoosts = selectedSpec.skillBoosts;
        this.specialization = selectedSpec;
        this.selectedSkillBoost = skillBoosts.get(generateRandomValueBetween(0, skillBoosts.size() - 1));
        this.unlockedSkillBoosts.add(selectedSkillBoost);
    }

    @Override
    public void generateStats() {

    }
}
