package top.oasismc.oasisrecipe.cmd.subcmd;

import org.bukkit.command.CommandSender;
import top.oasismc.oasisrecipe.OasisRecipe;
import top.oasismc.oasisrecipe.api.ISubCommand;
import top.oasismc.oasisrecipe.cmd.AbstractSubCommand;
import top.oasismc.oasisrecipe.recipe.handler.OldRecipeManager;
import top.oasismc.oasisrecipe.util.MsgUtil;

import java.util.List;

public final class ReloadCommand extends AbstractSubCommand {

    public static final ISubCommand INSTANCE = new ReloadCommand();

    private ReloadCommand() {
        super("reload", null);
        regSubCommand(new AbstractSubCommand("config", null) {
            @Override
            public boolean onCommand(CommandSender sender, List<String> args) {
                reloadConfigs();
                MsgUtil.sendMsg(sender, "commands.reloadConfig");
                return true;
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> args) {
        if (args.size() < 1) {
            reloadPlugin();
            MsgUtil.sendMsg(sender, "commands.reload");
            return true;
        }
        return super.onCommand(sender, args);
    }

    public static void reloadPlugin() {
        reloadConfigs();
        reloadRecipes();
    }

    public static void reloadConfigs() {
        OasisRecipe.getInstance().reloadConfig();
        OldRecipeManager.INSTANCE.getRecipeFile().reloadConfig();
        RemoveCommand.getRemovedRecipeConfig().reloadConfig();
    }

    public static void reloadRecipes() {
        OldRecipeManager.INSTANCE.reloadRecipes();
    }

}
