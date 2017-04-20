package ru.terra.jbrss.service;

import ru.terra.jbrss.db.entity.Settings;

public interface SettingsService {
    Settings get(String key);

    Settings save(Settings settings);

    void delete(String key);
}
