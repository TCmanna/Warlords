package com.ebicep.warlords.maps.option;

import com.ebicep.warlords.Warlords;
import com.ebicep.warlords.maps.Team;
import com.ebicep.warlords.maps.option.marker.DebugLocationMarker;
import com.ebicep.warlords.maps.option.marker.SpawnLocationMarker;
import com.ebicep.warlords.player.WarlordsPlayer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;

public class SpawnpointOption extends MarkerOption {

    public static final int BAD_TEAM_PENALTY = -10000;

    public SpawnpointOption(Location location, ToDoubleFunction<WarlordsPlayer> teamCheck, List<String> debugExtra) {
        super(
                new SpawnLocationMarker() {
                    @Override
                    public double getPriority(WarlordsPlayer player) {
                        return teamCheck.applyAsDouble(player);
                    }

                    @Override
                    public Location getLocation() {
                        return location;
                    }

                },
                DebugLocationMarker.create(Material.BED, 0, SpawnpointOption.class, "Spawnpoint", location, () -> debugExtra)
        );
    }

    public static SpawnpointOption forTeam(Location location, Team team) {
        return new SpawnpointOption(
                location,
                (p) -> p.getTeam() == team ? 0 : BAD_TEAM_PENALTY,
                Arrays.asList("Type: for-team", "Team: " + team)
        );
    }

    /**
     * A spawnpoint that looks at the player distribution throughout the game,
     * and sets its own priority to place the player closest to friendlies and
     * far away from enemies
     *
     * @param location The location of this point
     * @return
     */
    public static SpawnpointOption avoidingEnemyPlayers(Location location) {
        return new SpawnpointOption(
                location,
                (p) -> {
                    Location cache = new Location(null, 0, 0, 0);
                    Map<Boolean, Double> distances = p
                            .getGame()
                            .offlinePlayers()
                            .map(w -> Warlords.getPlayer(w.getKey()))
                            .filter(o -> o != null && o.isAlive())
                            .collect(Collectors.groupingBy(
                                    w -> p.getTeam() == null ? true : w.getTeam() == p.getTeam(),
                                    Collectors.averagingDouble(
                                            w -> Math.pow(w.getLocation(cache).distanceSquared(location), 0.25)
                                    )
                            ));
                    double distanceToEnemy = distances.getOrDefault(Boolean.FALSE, 0d);
                    double distanceToFriendlies = distances.getOrDefault(Boolean.TRUE, 0d);
                    return distanceToFriendlies - distanceToEnemy;
                },
                Arrays.asList("Type: avoiding-enemy-players")
        );
    }
}
