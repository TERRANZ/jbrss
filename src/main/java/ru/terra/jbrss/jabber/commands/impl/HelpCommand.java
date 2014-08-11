package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.commands.AbstractCommand;
import ru.terra.jbrss.jabber.commands.CommandsFactory;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:29
 */
@JabberCommand(name = "help")
public class HelpCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : new CommandsFactory().getCommandList()) {
            stringBuilder.append("'");
            stringBuilder.append(s);
            stringBuilder.append("' ");
        }
        sendMessage("This is help message, available commands: " + stringBuilder.toString());
        return true;
    }
}
