package ru.terra.jbrss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.db.repos.tenant.TenantSettingRepository;
import ru.terra.jbrss.service.SettingsService;

@Service("tenantSettingService")
public class TenantSettingServiceImpl implements SettingsService {
    @Autowired
    TenantSettingRepository repository;

    @Override
    public Settings findByKey(String updateInterval) {
        return repository.findByKey(updateInterval);
    }

    @Override
    public void save(Settings settings) {
        repository.save(settings);
    }
}
