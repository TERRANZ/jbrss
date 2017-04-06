package ru.terra.jbrss.im.core;

import java.util.List;

public abstract class AbstractCommand {
    protected String contact;
    protected ServerInterface serverInterface;

    public abstract boolean doCmd(String contact, List<String> params);

    public String name() {
        if (this.getClass().getAnnotation(IMCommand.class) != null)
            return this.getClass().getAnnotation(IMCommand.class).value();
        return "";
    }

    public boolean needAuth() {
        if (this.getClass().getAnnotation(IMCommand.class) != null)
            return this.getClass().getAnnotation(IMCommand.class).needAuth();
        return true;
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
