package ru.terra.jbrss.core.db.repos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.core.db.entity.Settings;

public interface SettingsRepository extends CrudRepository<Settings, Integer> {
    Settings findByKeyAndUserId(String key,Integer userId);
}
