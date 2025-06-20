package me.vkorostelev.jbrss;

import lombok.Getter;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class RssConfiguration {
    @Getter
    private final Configuration config;

    public RssConfiguration() {
        try {
            config = new Configurations().properties(new File("rss.properties"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
