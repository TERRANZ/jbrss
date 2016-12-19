package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand("default")
public class DefaultCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        sendMessage("Hello, this is default command output, send help for more information");
        return true;
    }
}
