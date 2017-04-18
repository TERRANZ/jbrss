package ru.terra.jbrss.service;

import ru.terra.jbrss.db.entity.Settings;

public interface SettingsService {
    Settings findByKey(String updateInterval);

    void save(Settings settings);
}
