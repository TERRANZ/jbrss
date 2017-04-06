package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.CommandsFactory;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "help", help = "Information about commands", needAuth = false)
public class HelpCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        StringBuilder helpInfo = new StringBuilder();
        CommandsFactory.getInstance().getHelps().forEach(c -> helpInfo.append(c).append("\n"));
        sendMessage(helpInfo.toString());
        return true;
    }
}
