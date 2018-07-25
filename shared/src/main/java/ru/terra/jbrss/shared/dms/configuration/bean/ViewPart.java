package ru.terra.jbrss.shared.dms.configuration.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Date: 26.05.14
 * Time: 15:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewPart implements Serializable {
    private String id, controllerType, name, pojo;
}
