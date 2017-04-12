package ru.terra.jbrss.db.repos;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.terra.jbrss.db.entity.Settings;

public interface SettingsRepository extends PagingAndSortingRepository<Settings, String> {
    Settings findByKeyAndUserId(String key, String userId);
}
