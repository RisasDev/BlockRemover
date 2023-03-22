package dev.risas.blockremover.listeners;

import dev.risas.blockremover.BlockRemover;
import dev.risas.blockremover.models.BlockRemoveManager;
import dev.risas.blockremover.models.BlockRemoveTask;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Risas
 * Project: BlockRemover
 * Date: 18-03-2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class BlockRemoveListener implements Listener {

    private final BlockRemover plugin;
    private final BlockRemoveManager blockRemoveManager;

    public BlockRemoveListener(BlockRemover plugin) {
        this.plugin = plugin;
        this.blockRemoveManager = plugin.getBlockRemoveManager();
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!blockRemoveManager.getWorlds().contains(player.getWorld())
                || player.hasPermission("blockremover.bypass")) return;

        Block block = event.getBlockPlaced();

        if (!blockRemoveManager.isAllRemoveBlocks() && !blockRemoveManager.getRemoveBlocks().containsKey(block.getType())) return;

        if (blockRemoveManager.getRemoveBlocksTime().isEmpty()) {
            BlockRemoveTask blockRemoveTask = new BlockRemoveTask(plugin);
            blockRemoveTask.start();
        }

        blockRemoveManager.addRemoveBlockTime(block);
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!blockRemoveManager.getWorlds().contains(player.getWorld())) return;

        Block block = event.getBlock();

        blockRemoveManager.getRemoveBlocksTime().remove(block);
    }
}
