package com.ebicep.warlords.player;

import com.ebicep.warlords.Warlords;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.ebicep.warlords.player.WeaponsRarity.*;

public enum Weapons {

    // common
    BLUDGEON("Bludgeon", new ItemStack(Material.SUGAR_CANE), COMMON),
    TRAINING_SWORD("Training Sword", new ItemStack(Material.SUGAR_CANE), COMMON),
    CLAWS("Claws", new ItemStack(Material.SUGAR_CANE), COMMON),
    SCIMITAR("Scimitar", new ItemStack(Material.SUGAR_CANE), COMMON),
    ORC_AXE("Orc Axe", new ItemStack(Material.SUGAR_CANE), COMMON),
    HATCHET("Hatchet", new ItemStack(Material.SUGAR_CANE), COMMON),
    PIKE("Pike", new ItemStack(Material.SUGAR_CANE), COMMON),
    HAMMER("Hammer", new ItemStack(Material.SUGAR_CANE), COMMON),
    WALKING_STICK("Walking Stick", new ItemStack(Material.SUGAR_CANE), COMMON),
    STEEL_SWORD("Steel Sword", new ItemStack(Material.SUGAR_CANE), COMMON),

    // rare
    WORLD_TREE_BRANCH("World Tree Branch", new ItemStack(Material.SUGAR_CANE), RARE),
    GEM_AXE("Gem Axe", new ItemStack(Material.SUGAR_CANE), RARE),
    DOUBLEAXE("Doubleaxe", new ItemStack(Material.SUGAR_CANE), RARE),
    MANDIBLES("Mandibles", new ItemStack(Material.SUGAR_CANE), RARE),
    GOLDEN_GLADIUS("Golden Gladius", new ItemStack(Material.SUGAR_CANE), RARE),
    STONE_MALLET("Stone Mallet", new ItemStack(Material.SUGAR_CANE), RARE),
    CUDGEL("Cudgel", new ItemStack(Material.SUGAR_CANE), RARE),
    VENOMSTRIKE("Venomstrike", new ItemStack(Material.SUGAR_CANE), RARE),
    HALBERD("Halberd", new ItemStack(Material.SUGAR_CANE), RARE),
    DEMONBLADE("Demonblade", new ItemStack(Material.SUGAR_CANE), RARE),

    // epic
    RUNEBLADE("Runeblade", new ItemStack(Material.SUGAR_CANE), EPIC),
    KATAR("Katar", new ItemStack(Material.SUGAR_CANE), EPIC),
    TENDERIZER("Tenderizer", new ItemStack(Material.SUGAR_CANE), EPIC),
    FLAMEWEAVER("Flameweaver", new ItemStack(Material.SUGAR_CANE), EPIC),
    NETHERSTEEL_KATANA("Nethersteel Katana", new ItemStack(Material.SUGAR_CANE), EPIC),
    RUNIC_AXE("Runic Axe", new ItemStack(Material.SUGAR_CANE), EPIC),
    NOMEGUSTA("Nomegusta", new ItemStack(Material.SUGAR_CANE), EPIC),
    LUNAR_RELIC("Lunar Relic", new ItemStack(Material.SUGAR_CANE), EPIC),
    DIVINE_REACH("Divine Reach", new ItemStack(Material.SUGAR_CANE), EPIC),
    GEMCRUSHER("Gemcrusher", new ItemStack(Material.SUGAR_CANE), EPIC),
    ELVEN_GREATSWORD("Elven Greatsword", new ItemStack(Material.SUGAR_CANE), EPIC),
    HAMMER_OF_LIGHT("Hammer of Light", new ItemStack(Material.SUGAR_CANE), EPIC),
    MAGMASWORD("Magmasword", new ItemStack(Material.SUGAR_CANE), EPIC),
    DIAMONDSPARK("Diamondspark", new ItemStack(Material.SUGAR_CANE), EPIC),
    ZWEIREAPER("Zweireaper", new ItemStack(Material.SUGAR_CANE), EPIC),

    // legendary
    VOID_EDGE("Void Edge", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    FELFLAME_BLADE("Felflame Blade", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    AMARANTH("Amaranth", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    ARMBLADE("Armblade", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    GEMINI("Gemini", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    DRAKEFANG("Drakefang", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    ABBADON("Abbadon", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    FROSTBITE("Frostbite", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    BROCCOMACE("Broccomace", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    VOID_TWIG("Void Twig", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    RUBY_THORN("Ruby Thorn", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    ENDERFIST("Enderfist", new ItemStack(Material.SUGAR_CANE), LEGENDARY),

    // wl 2 exclusive
    NEW_LEAF_SCYTHE("Daphne's Harvest", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    NEW_LEAF_AXE("Fate of Daphne", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    NEW_LEAF_SWORD("Canopy's Jade Edge", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    NEW_LEAF_SPEAR("Daphne's Viper", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SCYTHE("Tenth Plague", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SWORD("Hyperion's Awakening", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SWORD_2("Blazeguard", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SWORD_3("Venom", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SWORD_4("Lilium", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_HAMMER("Wrath of Aether", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_STAFF("Wit of Oblivion", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_STAFF_2("Lament", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SCIMITAR("Bloodquench", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_TRIDENT("Torment", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    SILVER_PHANTASM_SAWBLADE("Slayer", new ItemStack(Material.SUGAR_CANE), LEGENDARY),
    CANDY_CANE("Candy Slapper", new ItemStack(Material.SUGAR_CANE), LEGENDARY),

    // mythic
    FABLED_HEROICS_SCYTHE("Ghostly Sickles", new ItemStack(Material.SUGAR_CANE), MYTHIC),
    FABLED_HEROICS_SWORD("Nichirin", new ItemStack(Material.SUGAR_CANE), MYTHIC),
    FABLED_HEROICS_SWORD_2("Bumbleblade", new ItemStack(Material.SUGAR_CANE), MYTHIC),

    ;

    public final String name;
    public final ItemStack item;
    public final WeaponsRarity rarity;
    public boolean isUnlocked;

    Weapons(String name, ItemStack item, WeaponsRarity rarity) {
        this.name = name;
        this.item = item;
        this.rarity = rarity;
        this.isUnlocked = rarity != MYTHIC;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
    }

    public WeaponsRarity getRarity() {
        return this.rarity;
    }

    public String getName() {
        return this.name;
    }

    @Deprecated
    public static Weapons getSelected(OfflinePlayer player, Specializations specializations) {
        return Warlords.getPlayerSettings(player.getUniqueId()).getWeaponSkins().getOrDefault(specializations, FELFLAME_BLADE);
    }

    @Deprecated
    public static void setSelected(OfflinePlayer player, Specializations specializations, Weapons weapon) {
        Warlords.getPlayerSettings(player.getUniqueId()).getWeaponSkins().put(specializations, weapon);
    }

    public static Weapons getWeapon(String name) {
        if(name == null) {
            return FELFLAME_BLADE;
        }
        for (Weapons value : Weapons.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return FELFLAME_BLADE;
    }
}
