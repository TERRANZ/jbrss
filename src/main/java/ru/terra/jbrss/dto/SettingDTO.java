package ru.terra.jbrss.dto;

import ru.terra.server.dto.CommonDTO;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Date: 23.05.14
 * Time: 11:58
 */
@XmlRootElement
public class SettingDTO extends CommonDTO {
    public String key, value;

    public SettingDTO() {
    }

    public SettingDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
