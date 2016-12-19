package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand("login")
public class LoginCommand extends AbstractCommand {
    private enum PARAMS {
        LOGIN, PASS
    }

    @Override
    public boolean doCmd(String contact, List<String> params) {
        if (serverInterface.isContactExists(contact)) {
            sendMessage("You already logged in");
            return false;
        }
        if (params.size() < 2) {
            sendMessage("Not enough params, valid params login password");
            return false;
        }
        String login = params.get(PARAMS.LOGIN.ordinal());
        String pass = params.get(PARAMS.PASS.ordinal());
        Integer userId = serverInterface.login(login, pass);
        if (userId == null) {
            sendMessage("You have entered invalid login and password");
            return false;
        }
        if (serverInterface.isContactAttached(contact, login)) {
            sendMessage("You already logged in and your contact attached");
            return false;
        }
        serverInterface.attachContactToUser(contact, userId);
        sendMessage("Your contact " + contact + " attached to user " + login);
        return true;
    }
}
