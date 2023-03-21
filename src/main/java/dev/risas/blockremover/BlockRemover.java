package dev.risas.blockremover;

import dev.risas.blockremover.commands.BlockRemoverCommand;
import dev.risas.blockremover.listeners.BlockRemoveListener;
import dev.risas.blockremover.models.BlockRemoveManager;
import dev.risas.blockremover.utilities.file.FileConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Risas
 * Project: BlockRemover
 * Date: 3/20/2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

@Getter
public class BlockRemover extends JavaPlugin {

    private FileConfig configFile;
    private BlockRemoveManager blockRemoveManager;

    @Override
    public void onEnable() {
        this.configFile = new FileConfig(this, "config.yml");
        this.blockRemoveManager = new BlockRemoveManager(this);
        this.getCommand("blockremover").setExecutor(new BlockRemoverCommand(this));
        Bukkit.getPluginManager().registerEvents(new BlockRemoveListener(this), this);
    }

    @Override
    public void onDisable() {
        this.blockRemoveManager.onDisable();
    }

    public void onReload() {
        this.configFile.reload();
        this.blockRemoveManager.onReload();
    }
}
