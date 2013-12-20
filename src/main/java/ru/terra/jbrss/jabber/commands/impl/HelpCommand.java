package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:29
 */
@JabberCommand(name = "help")
public class HelpCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        serverInterface.sendMessage(contact, "This is help message, available commands: help");
        return true;
    }
}
