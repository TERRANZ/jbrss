package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

@IMCommand("default")
public class DefaultCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, String[] params) {
        sendMessage("Hello, this is default command output, send help for more information");
        return true;
    }
}
