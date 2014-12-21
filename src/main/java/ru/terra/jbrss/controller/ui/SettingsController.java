package ru.terra.jbrss.controller.ui;

import com.sun.jersey.api.core.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.db.entity.Settings;
import ru.terra.jbrss.dto.SettingDTO;
import ru.terra.jbrss.engine.SettingsEngine;
import ru.terra.server.constants.ErrorConstants;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.ListDTO;
import ru.terra.server.dto.SimpleDataDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Date: 23.05.14
 * Time: 14:45
 */
@Path(URLConstants.DoJson.Settings.SETTINGS)
public class SettingsController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SettingsEngine settingsEngine = new SettingsEngine();

    @GET
    @Path(URLConstants.DoJson.Settings.GET_SETTINGS)
    public ListDTO<SettingDTO> getSettings(@Context HttpContext hc) {
        ListDTO<SettingDTO> ret = new ListDTO<>();
        if (!isAuthorized(hc)) {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
        ret.setData(settingsEngine.listByUser(getCurrentUserId(hc)));
        return ret;
    }

    @GET
    @Path(URLConstants.DoJson.Settings.GET_SETTING)
    public SettingDTO getSetting(@Context HttpContext hc, @QueryParam("key") String key) {
        SettingDTO ret = new SettingDTO();
        if (!isAuthorized(hc)) {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
        Settings s = settingsEngine.findByKey(key, getCurrentUserId(hc));
        if (s == null) {
            ret.errorCode = 2;
            ret.errorMessage = "Not found";
            return ret;

        }

        return settingsEngine.entityToDto(s);
    }

    @GET
    @Path(URLConstants.DoJson.Settings.SET_SETTINGS)
    public SimpleDataDTO<Boolean> setSetting(@Context HttpContext hc, @QueryParam("key") String key, @QueryParam("val") String val) {
        SimpleDataDTO<Boolean> ret = new SimpleDataDTO<>();
        if (!isAuthorized(hc)) {
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
        return new SimpleDataDTO<>(settingsEngine.setSetting(key, val, getCurrentUserId(hc)));
    }
}
