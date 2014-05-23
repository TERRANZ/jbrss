package ru.terra.jbrss.dto;

import ru.terra.jbrss.db.entity.Settings;
import ru.terra.server.dto.CommonDTO;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Date: 23.05.14
 * Time: 11:58
 */
@XmlRootElement
public class SettingDTO extends CommonDTO {
    public String key, value;
    public Integer uid;

    public SettingDTO(Settings parent) {
        this.key = parent.getKey();
        this.value = parent.getValue();
        this.uid = parent.getUserId();
    }

    public SettingDTO() {
    }

    public SettingDTO(String key, String value, Integer uid) {
        this.key = key;
        this.value = value;
        this.uid = uid;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
