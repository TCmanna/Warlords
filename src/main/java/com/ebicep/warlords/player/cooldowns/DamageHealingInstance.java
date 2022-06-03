package com.ebicep.warlords.player.cooldowns;

import com.ebicep.warlords.events.WarlordsDamageHealingEvent;

public interface DamageHealingInstance {

    /**
     * if true, only the methods of the first cooldown is applied, the rest are skipped,
     * checks based on class and name
     */
    default boolean distinct() {
        return false;
    }

    /**
     * boolean if damageheal instance is healing
     */
    default boolean isHealing() {
        return false;
    }

    //HEALING

    /**
     * Done before any healing is done to players
     *
     * @param event            WarlordsDamageHealingEvent
     * @param currentHealValue The current heal value
     */
    default float doBeforeHealFromSelf(WarlordsDamageHealingEvent event, float currentHealValue) {
        return currentHealValue;
    }


    //DAMAGE


    /**
     * Calls before any reduction - based on self cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     */
    default void doBeforeReductionFromSelf(WarlordsDamageHealingEvent event) {

    }

    /**
     * Calls before any reduction - based on attackers cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     */
    default void doBeforeReductionFromAttacker(WarlordsDamageHealingEvent event) {

    }

    /**
     * If attacker has abilities that increase their crit chance (inferno)
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentCritChance The current crit chance
     */
    default int addCritChanceFromAttacker(WarlordsDamageHealingEvent event, int currentCritChance) {
        return currentCritChance;
    }

    /**
     * If attacker has abilities that increase their crit multiplier (inferno)
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentCritMultiplier The current crit multiplier
     */
    default int addCritMultiplierFromAttacker(WarlordsDamageHealingEvent event, int currentCritMultiplier) {
        return currentCritMultiplier;
    }

    /**
     * If self has abilities that increase/decrease their damage taken (berserk) - before intervene
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default float modifyDamageBeforeInterveneFromSelf(WarlordsDamageHealingEvent event, float currentDamageValue) {
        return currentDamageValue;
    }

    /**
     * If attacker has abilities that increase/decrease damage done (berserk) - before intervene
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default float modifyDamageBeforeInterveneFromAttacker(WarlordsDamageHealingEvent event, float currentDamageValue) {
        return currentDamageValue;
    }

    /**
     * Called after when the player takes damage while intervened - based on attackers cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onInterveneFromAttacker(WarlordsDamageHealingEvent event, float currentDamageValue) {
    }

    /**
     * If self has abilities that increase/decrease their damage taken (berserk) - after intervene
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default float modifyDamageAfterInterveneFromSelf(WarlordsDamageHealingEvent event, float currentDamageValue) {
        return currentDamageValue;
    }

    /**
     * If attacker has abilities that increase/decrease damage done (berserk) - after intervene
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default float modifyDamageAfterInterveneFromAttacker(WarlordsDamageHealingEvent event, float currentDamageValue) {
        return currentDamageValue;
    }

    /**
     * Called after when the player takes shield damage - based on self cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onShieldFromSelf(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit) {
    }

    /**
     * Called after when the player takes shield damage - based on attackers cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onShieldFromAttacker(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit) {
    }

    /**
     * Called after all damage modifications and after the damage has been applied - based on self cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onDamageFromSelf(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit) {
    }

    /**
     * Called after all damage modifications and after the damage has been applied - based on attackers cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onDamageFromAttacker(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit) {
    }

    /**
     * Called if the player dies - based on all enemies cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     * @param isCrit If the damage was a critical hit
     * @param isKiller If the attacker was the killer
     */
    default void onDeathFromEnemies(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit, boolean isKiller) {

    }

    /**
     * Called at the end of damage instance - based on self cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onEndFromSelf(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit) {
    }

    /**
     * Called at the end of damage instance - based on attackers cooldowns
     *
     * @param event WarlordsDamageHealingEvent
     * @param currentDamageValue The current damage value
     */
    default void onEndFromAttacker(WarlordsDamageHealingEvent event, float currentDamageValue, boolean isCrit) {
    }
}
