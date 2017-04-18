package ru.terra.jbrss.db.repos;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.terra.jbrss.db.entity.Settings;

@NoRepositoryBean
public interface BaseSettingsRepository extends PagingAndSortingRepository<Settings, String> {
    Settings findByKey(String key);
}
