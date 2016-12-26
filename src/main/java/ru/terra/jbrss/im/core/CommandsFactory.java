package ru.terra.jbrss.im.core;

import ru.terra.jbrss.core.ClassSearcher;
import ru.terra.jbrss.im.core.commands.DefaultCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsFactory {
    private Map<String, AbstractCommand> commands = new HashMap<>();
    private List<String> helps = new ArrayList<>();
    private static CommandsFactory instance = new CommandsFactory();

    public static CommandsFactory getInstance() {
        return instance;
    }

    private CommandsFactory() {
        for (AbstractCommand command : new ClassSearcher<AbstractCommand>().load("ru.terra.jbrss.im.core.commands", IMCommand.class)) {
            if (command != null) {
                commands.put(command.name(), command);
                helps.add(command.getClass().getAnnotation(IMCommand.class).help());
            }
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

    public List<String> getHelps() {
        return helps;
    }
}
