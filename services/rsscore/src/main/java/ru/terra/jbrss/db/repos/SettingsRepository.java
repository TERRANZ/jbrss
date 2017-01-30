package ru.terra.jbrss.db.repos;


import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.db.entity.Settings;

public interface SettingsRepository extends CrudRepository<Settings, Integer> {
    Settings findByKeyAndUserId(String key, Integer userId);
}
