package top.oasismc.oasisrecipe.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.oasismc.oasisrecipe.api.cmd.ISubCommand;
import top.oasismc.oasisrecipe.util.LangUtil;
import top.oasismc.oasisrecipe.util.MapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractSubCommand implements ISubCommand {

    private final String command;
    private Map<String, ISubCommand> subCommandMap;

    public AbstractSubCommand(String command, Map<String, ISubCommand> subCommandMap) {
        this.command = command;
        this.subCommandMap = subCommandMap;
    }

    public AbstractSubCommand(String command) {
        this(command, new ConcurrentHashMap<>());
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> args) {
        if (subCommandMap == null || args.size() < 1) {
            sendNotEnoughCmdParamMsg(sender, "unknown");
            return true;
        }
        ISubCommand subCommand = subCommandMap.get(args.get(0));
        if (subCommand == null) {
            LangUtil.sendMsg(sender, "command.undefined_subcmd");
        } else {
            subCommand.onCommand(sender, args.subList(1, args.size()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        if (subCommandMap == null)
            return Collections.singletonList("");
        if (args.size() <= 1) {
            List<String> returnList = new ArrayList<>(subCommandMap.keySet());
            returnList.removeIf(str -> !str.startsWith(args.get(0)));
            return returnList;
        }
        ISubCommand subCmd = subCommandMap.get(args.get(0));
        if (subCmd != null)
            return subCommandMap.get(args.get(0)).onTabComplete(sender, args.subList(1, args.size()));
        return Collections.singletonList("");
    }

    @Override
    public String getSubCommand() {
        return command;
    }

    @Override
    public Map<String, ISubCommand> getSubCommands() {
        return Collections.unmodifiableMap(subCommandMap);
    }

    @Override
    public void regSubCommand(ISubCommand command) {
        if (subCommandMap == null) {
            subCommandMap = new ConcurrentHashMap<>();
        }
        subCommandMap.put(command.getSubCommand(), command);
    }

    public void setSubCommandMap(Map<String, ISubCommand> subCommandMap) {
        this.subCommandMap = subCommandMap;
    }

    public void sendNotEnoughCmdParamMsg(CommandSender sender, int paramNum) {
        sendNotEnoughCmdParamMsg(sender, String.valueOf(paramNum));
    }

    public void sendNotEnoughCmdParamMsg(CommandSender sender, String paramStr) {
        LangUtil.sendMsg(sender, "command.not_enough_param", MapUtil.newHashMap("<number>", paramStr));
    }

    public boolean checkSenderIsPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        } else {
            LangUtil.sendMsg(sender, "command.player_only");
            return false;
        }
    }

}
