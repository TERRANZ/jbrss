package ru.terra.jbrss.jabber.commands.impl;

import ru.terra.jbrss.jabber.JabberModel;
import ru.terra.jbrss.jabber.commands.AbstractCommand;
import ru.terra.jbrss.jabber.commands.JabberCommand;

/**
 * Date: 19.12.13
 * Time: 15:24
 */
@JabberCommand(name = "reg")
public class RegCommand extends AbstractCommand {
    private JabberModel model = new JabberModel();


    @Override
    public boolean doCmd(String contact, String[] params) {
        String captcha = params[1];
        String login = params[2];
        String pass = params[3];
        if (model.completeReg(contact, login, pass, captcha))
            sendMessage("Registration complete!");
        else
            sendMessage("Registration INCOMPLETE");
        return true;
    }
}
