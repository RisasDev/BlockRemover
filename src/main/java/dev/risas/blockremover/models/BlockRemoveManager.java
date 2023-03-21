package dev.risas.blockremover.models;

import dev.risas.blockremover.BlockRemover;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.*;

/**
 * Created by Risas
 * Project: BlockRemover
 * Date: 17-03-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter
public class BlockRemoveManager {

    private final BlockRemover plugin;

    private final List<World> worlds;
    private final Map<Material, Long> removeBlocks;
    private final Map<Location, Long> removeBlocksTime;
    private final Set<Location> removeBlocksLocations;

    public BlockRemoveManager(BlockRemover plugin) {
        this.plugin = plugin;
        this.worlds = new ArrayList<>();

        for (String worldName : plugin.getConfig().getStringList("block-remove-system.worlds")) {
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                plugin.getLogger().warning("World '" + worldName + "' does not exist.");
                continue;
            }

            this.worlds.add(world);
        }

        this.removeBlocks = new HashMap<>();
        this.removeBlocksTime = new HashMap<>();
        this.removeBlocksLocations = new HashSet<>();
        this.loadBlocks();
    }

    public long getRemoveBlockTimeRemaining(Location location) {
        return removeBlocksTime.get(location) - System.currentTimeMillis();
    }

    public void addRemoveBlockTime(Location location, Material material) {
        long time = System.currentTimeMillis() + removeBlocks.get(material);
        removeBlocksTime.put(location, time);
    }

    public void loadBlocks() {
        for (String block : plugin.getConfigFile().getStringList("block-remove-system.blocks")) {
            String[] blockSplit = block.split(":");
            Material material = Material.matchMaterial(blockSplit[0]);
            long time = 1000L * Integer.parseInt(blockSplit[1]);

            removeBlocks.put(material, time);
        }
    }

    public void onDisable() {
        for (Location location : removeBlocksTime.keySet()) {
            location.getBlock().setType(Material.AIR);
        }
    }

    public void onReload() {
        this.removeBlocks.clear();
        this.loadBlocks();
    }
}
