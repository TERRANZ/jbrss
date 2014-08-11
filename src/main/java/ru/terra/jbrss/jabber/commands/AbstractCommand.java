package ru.terra.jbrss.jabber.commands;

import ru.terra.jbrss.jabber.ServerInterface;

/**
 * Date: 19.12.13
 * Time: 15:21
 */
public abstract class AbstractCommand {
    protected String contact;
    protected ServerInterface serverInterface;

    public abstract boolean doCmd(String contact, String[] params);

    public String name() {
        if (this.getClass().getAnnotation(JabberCommand.class) != null)
            return this.getClass().getAnnotation(JabberCommand.class).name();
        return "";
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    public void sendMessage(String msg) {
        if (serverInterface != null)
            serverInterface.sendMessage(contact, msg);
    }
}
