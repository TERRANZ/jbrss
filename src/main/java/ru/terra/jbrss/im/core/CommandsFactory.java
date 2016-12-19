package ru.terra.jbrss.im.core;

import ru.terra.jbrss.core.ClassSearcher;
import ru.terra.jbrss.im.core.commands.DefaultCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsFactory {
    private Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandsFactory() {
        for (AbstractCommand command : new ClassSearcher<AbstractCommand>().load("ru.terra.jbrss.im.core.commands", IMCommand.class)) {
            if (command != null)
                commands.put(command.name(), command);
        }
    }

    public AbstractCommand getCommand(String name) {
        if (name.startsWith("/"))
            name = name.substring(1, name.length());
        AbstractCommand ret = commands.get(name);
        if (ret != null)
            return ret;
        return new DefaultCommand();
    }

    public List<String> getCommandList() {
        return new ArrayList<>(commands.keySet());
    }
}
