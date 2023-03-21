package dev.risas.blockremover.commands;

import dev.risas.blockremover.BlockRemover;
import dev.risas.blockremover.utilities.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Risas
 * Project: BlockRemover
 * Date: 3/20/2023
 * Twitter: @RisasDev
 * GitHub: https://github.com/RisasDev
 */

public class BlockRemoverCommand implements CommandExecutor {

    private final BlockRemover plugin;

    public BlockRemoverCommand(BlockRemover plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("blockremover.admin")) {
            ChatUtil.sendMessage(sender, "&cYou do not have permission to execute this command.");
            return true;
        }

        if (args.length == 0) {
            ChatUtil.sendMessage(sender, new String[]{
                    ChatUtil.NORMAL_LINE,
                    "&6&lBlockRemover Commands",
                    "",
                    " &7▶ &e/" + label + " reload &7- &fReload the plugin.",
                    ChatUtil.NORMAL_LINE
            });
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.onReload();
            ChatUtil.sendMessage(sender, "&aBlockRemover has been reloaded!");
        }
        return true;
    }
}
