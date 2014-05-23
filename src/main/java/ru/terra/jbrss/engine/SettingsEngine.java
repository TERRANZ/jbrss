package ru.terra.jbrss.engine;

import ru.terra.jbrss.db.controllers.SettingsJpaController;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.dto.SettingDTO;
import ru.terra.server.engine.AbstractEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 23.05.14
 * Time: 11:58
 */
public class SettingsEngine extends AbstractEngine<Settings, SettingDTO> {
    public SettingsEngine() {
        super(new SettingsJpaController());
    }

    @Override
    public SettingDTO getDto(Integer id) {
        return new SettingDTO(getBean(id));
    }

    @Override
    public void dtoToEntity(SettingDTO dto, Settings settings) {
        if (settings == null)
            settings = new Settings();
        settings.setKey(dto.getKey());
        settings.setValue(dto.getValue());
        settings.setUserId(dto.getUid());
    }

    @Override
    public SettingDTO entityToDto(Settings settings) {
        return new SettingDTO(settings);
    }

    public List<SettingDTO> listByUser(Integer uid) {
        List<SettingDTO> ret = new ArrayList<>();
        for (Settings ent : ((SettingsJpaController) dbController).findByUser(uid))
            ret.add(entityToDto(ent));
        return ret;
    }

    public Settings findByKey(String key, Integer uid) {
        return ((SettingsJpaController) dbController).findByKey(key, uid);
    }
}
