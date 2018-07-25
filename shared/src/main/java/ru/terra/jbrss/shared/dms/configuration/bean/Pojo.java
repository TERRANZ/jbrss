package ru.terra.jbrss.shared.dms.configuration.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Map;

/**
 * Date: 02.06.14
 * Time: 13:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pojo implements Serializable {
    private String name, type, parent;
    private Map<String, String> fields;
}
