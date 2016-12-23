package ru.terra.jbrss.im.core.commands;

import ru.terra.jbrss.im.core.AbstractCommand;
import ru.terra.jbrss.im.core.IMCommand;

import java.util.List;

@IMCommand(value = "set", help = "Set settings command, syntax: set command value")
public class SetCommand extends AbstractCommand {
    @Override
    public boolean doCmd(String contact, List<String> params) {
        return true;
    }
}
