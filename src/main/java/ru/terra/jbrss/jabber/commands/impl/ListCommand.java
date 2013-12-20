package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:25
 */
@JabberCommand(name = "list")
public class ListCommand extends AbstractPrivCommand {
    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        return false;
    }
}
