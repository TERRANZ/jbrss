package ru.terra.jbrss.engine;

import ru.terra.jbrss.db.controllers.SettingsJpaController;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.dto.SettingDTO;
import ru.terra.server.engine.AbstractEngine;

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
        return null;
    }

    @Override
    public void dtoToEntity(SettingDTO dto, Settings settings) {

    }

    @Override
    public SettingDTO entityToDto(Settings settings) {
        return null;
    }
}
