package ru.terra.jbrss.jabber.commands;

import ru.terra.jbrss.jabber.ServerInterface;

/**
 * Date: 19.12.13
 * Time: 15:21
 */
public abstract class AbstractCommand {
    public abstract boolean doCmd(String contact, String[] params, ServerInterface serverInterface);

    public String name() {
        if (this.getClass().getAnnotation(JabberCommand.class) != null)
            return this.getClass().getAnnotation(JabberCommand.class).name();
        return "";
    }
}
