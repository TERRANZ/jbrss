package ru.terra.jbrss.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettingsDto {
    public String key;
    public String value;
}
