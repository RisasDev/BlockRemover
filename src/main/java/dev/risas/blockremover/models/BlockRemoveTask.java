package dev.risas.blockremover.models;

import dev.risas.blockremover.BlockRemover;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by Risas
 * Project: BlockRemover
 * Date: 02-02-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class BlockRemoveTask implements Runnable {

    private final BlockRemover plugin;
    private int taskId;
    private final BlockRemoveManager blockRemoveManager;

    public BlockRemoveTask(BlockRemover plugin) {
        this.plugin = plugin;
        this.blockRemoveManager = plugin.getBlockRemoveManager();
    }

    @Override
    public void run() {
        if (blockRemoveManager.getRemoveBlocksTime().isEmpty()) {
            cancel();
            return;
        }
        
        for (Block block : blockRemoveManager.getRemoveBlocksTime().keySet()) {
            long time = blockRemoveManager.getRemoveBlockTimeRemaining(block);

            if (time <= 0) {
                block.setType(Material.AIR);
                blockRemoveManager.getRemoveBlocksCache().add(block);
            }
        }

        blockRemoveManager.getRemoveBlocksCache().forEach(blockRemoveManager.getRemoveBlocksTime()::remove);
        blockRemoveManager.getRemoveBlocksCache().clear();
    }

    public void start() {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
