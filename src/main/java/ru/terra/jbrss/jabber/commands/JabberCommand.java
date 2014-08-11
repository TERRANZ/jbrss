package ru.terra.jbrss.jabber.commands;

import java.lang.annotation.*;

/**
 * Date: 19.12.13
 * Time: 15:23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JabberCommand {
    String name();
}
