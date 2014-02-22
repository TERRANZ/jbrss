package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.JabberModel;
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
    public boolean doCmd(String contact, String[] params) {
        if (model.isContactExists(contact)) {
            if (model.getContactStatus(contact) == ContactStatus.CAPTCHA_SENT) {
                sendMessage("You are already started registration");
                sendMessage("open captcha image from that " + model.proceedRegContact(contact) + " url and then process to reg command");
                sendMessage("reg command params: captcha login pass");
            } else {
                serverInterface.sendMessage(contact, "You are already registered!");
            }
        } else {
            String captcha = model.startRegContact(contact);
            sendMessage("You are starting reg process, open captcha image from that " + captcha + " url and then process to reg command");
            sendMessage("reg command params: captcha login pass");
        }
        return true;
    }
}
