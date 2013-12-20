package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.engine.UsersEngine;
import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 20.12.13
 * Time: 16:37
 */
@JabberCommand(name = "attach")
public class AttachCommand extends AbstractPrivCommand {

    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        if (model.isContactExists(contact)) {
            serverInterface.sendMessage(contact, "this " + contact + " account is already attached to user");
            return true;
        } else {
            String user = params[1];
            String pass = params[2];
            UsersEngine ue = new UsersEngine();
            User u = ue.login(user, pass);
            if (u == null) {
                serverInterface.sendMessage(contact, "User name or password is invalid");
                return true;
            } else {
                model.attachUserToContact(contact, u.getId());
                serverInterface.sendMessage(contact, "OK, user " + u.getName() + " is now yours");
            }
        }
        return true;
    }
}
