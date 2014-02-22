package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.commands.AbstractCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 20.12.13
 * Time: 15:26
 */
@JabberCommand(name = "default")
public class DefaultCommand extends AbstractCommand {

    @Override
    public boolean doCmd(String contact, String[] params) {
        sendMessage("Hello, this is default command output, send help for more information");
        return true;
    }
}
