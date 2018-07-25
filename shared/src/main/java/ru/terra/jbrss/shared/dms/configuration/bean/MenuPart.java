package ru.terra.jbrss.shared.dms.configuration.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

/**
 * Date: 26.05.14
 * Time: 15:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuPart implements Serializable {
    public enum MenuPartType {
        VIEWPART, SPLITTER
    }

    private String text, shortcut, viewPart, uid = UUID.randomUUID().toString();
    private MenuPartType type;
}



