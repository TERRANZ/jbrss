package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.db.entity.User;
import ru.terra.jbrss.engine.UsersEngine;
import ru.terra.jbrss.jabber.commands.AbstractPrivCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 20.12.13
 * Time: 16:37
 */
@JabberCommand(name = "attach")
public class AttachCommand extends AbstractPrivCommand {

    @Override
    public boolean doCmd(String contact, String[] params) {
        if (jabberModel.isContactExists(contact)) {
            sendMessage("this " + contact + " account is already attached to user");
            return true;
        } else {
            String user = params[1];
            String pass = params[2];
            UsersEngine ue = new UsersEngine();
            User u = ue.login(user, pass);
            if (u == null) {
                sendMessage("User name or password is invalid");
                return true;
            } else {
                jabberModel.attachUserToContact(contact, u.getId());
                sendMessage("OK, user " + u.getName() + " is now yours");
            }
        }
        return true;
    }
}
