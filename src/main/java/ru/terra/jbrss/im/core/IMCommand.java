package ru.terra.jbrss.im.core;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IMCommand {
    String name();
    String type();
}
