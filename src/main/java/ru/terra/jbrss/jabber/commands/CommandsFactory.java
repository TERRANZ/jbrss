package ru.terra.jbrss.jabber.commands;

import ru.terra.jbrss.engine.ClassSearcher;
import ru.terra.jbrss.jabber.commands.impl.DefaultCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 20.12.13
 * Time: 15:30
 */
public class CommandsFactory {
    private Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandsFactory() {
        for (AbstractCommand command : new ClassSearcher<AbstractCommand>().load("ru.terra.jbrss.jabber.commands.impl", JabberCommand.class)) {
            if (command != null)
                commands.put(command.name(), command);
        }
    }

    public AbstractCommand getCommand(String name) {
        AbstractCommand ret = commands.get(name);
        if (ret != null)
            return ret;
        return new DefaultCommand();
    }
}
