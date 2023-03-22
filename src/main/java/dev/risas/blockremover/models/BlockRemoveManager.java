package dev.risas.blockremover.models;

import dev.risas.blockremover.BlockRemover;
import dev.risas.blockremover.utilities.TimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

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
    private final Map<Block, Long> removeBlocksTime;
    private final Set<Block> removeBlocksCache;
    private boolean allRemoveBlocks;
    private long allRemoveBlocksTime;

    public BlockRemoveManager(BlockRemover plugin) {
        this.plugin = plugin;
        this.worlds = new ArrayList<>();
        this.removeBlocks = new HashMap<>();
        this.removeBlocksTime = new HashMap<>();
        this.removeBlocksCache = new HashSet<>();
        this.onLoad();
    }

    public long getRemoveBlockTimeRemaining(Block block) {
        return removeBlocksTime.get(block) - System.currentTimeMillis();
    }

    public void addRemoveBlockTime(Block block) {
        long time = System.currentTimeMillis() + (allRemoveBlocks ? allRemoveBlocksTime : removeBlocks.get(block.getType()));
        removeBlocksTime.put(block, time);
    }

    public void onLoad() {
        for (String worldName : plugin.getConfigFile().getStringList("block-remove-system.worlds")) {
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                plugin.getLogger().warning("World '" + worldName + "' does not exist.");
                continue;
            }

            this.worlds.add(world);
        }

        this.allRemoveBlocks = plugin.getConfigFile().getBoolean("block-remove-system.blocks.all.enabled");
        this.allRemoveBlocksTime = TimeUtil.formatLong(plugin.getConfigFile().getString("block-remove-system.blocks.all.time"));

        for (String block : plugin.getConfigFile().getStringList("block-remove-system.blocks.specifics")) {
            String[] blockSplit = block.split(":");
            Material material = Material.matchMaterial(blockSplit[0]);
            long time = TimeUtil.formatLong(blockSplit[1]);

            removeBlocks.put(material, time);
        }
    }

    public void onReload() {
        this.removeBlocks.clear();
        this.onLoad();
    }

    public void onDisable() {
        for (Block block : removeBlocksTime.keySet()) {
            block.setType(Material.AIR);
        }
    }
}
