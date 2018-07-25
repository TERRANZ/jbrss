package ru.terra.jbrss.shared.dms.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.terra.jbrss.shared.dms.configuration.bean.MenuPart;
import ru.terra.jbrss.shared.dms.configuration.bean.Pojo;
import ru.terra.jbrss.shared.dms.configuration.bean.ViewPart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 26.05.14
 * Time: 15:29
 */
@Data
@NoArgsConstructor
public class Configuration implements Serializable {
    private String name, comment;
    private List<MenuPart> menus = new ArrayList<>();
    private List<ViewPart> viewParts = new ArrayList<>();
    private List<Pojo> pojos = new ArrayList<>();

    public ViewPart getViewPart(String name) {
        for (ViewPart viewPart : viewParts)
            if (viewPart.getName().equals(name))
                return viewPart;
        return null;
    }

    public Pojo getPojo(String type) {
        for (Pojo pojo : pojos)
            if (pojo.getType().equals(type))
                return pojo;
        return null;
    }
}
