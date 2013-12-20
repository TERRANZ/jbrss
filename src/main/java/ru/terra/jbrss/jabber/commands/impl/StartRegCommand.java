package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.JabberModel;
import ru.terra.jbrss.jabber.ServerInterface;
import ru.terra.jbrss.jabber.commands.AbstractCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;
import ru.terra.jbrss.jabber.db.entity.ContactStatus;

/**
 * Date: 20.12.13
 * Time: 16:09
 */
@JabberCommand(name = "startreg")
public class StartRegCommand extends AbstractCommand {
    private JabberModel model = new JabberModel();

    @Override
    public boolean doCmd(String contact, String[] params, ServerInterface serverInterface) {
        if (model.isContactExists(contact)) {
            if (model.getContactStatus(contact) == ContactStatus.CAPTCHA_SENT) {
                serverInterface.sendMessage(contact, "You are already started registration");
                String captcha = model.proceedRegContact(contact);
                serverInterface.sendMessage(contact, "open captcha image from that " + captcha + " url and then process to reg command");
                serverInterface.sendMessage(contact, "reg command params: captcha login pass");
            } else {
                serverInterface.sendMessage(contact, "You are already registered!");
            }
        } else {
            String captcha = model.startRegContact(contact);
            serverInterface.sendMessage(contact, "You are starting reg process, open captcha image from that " + captcha + " url and then process to reg command");
            serverInterface.sendMessage(contact, "reg command params: captcha login pass");
        }
        return true;
    }
}
