package ru.terra.jbrss.controller;


import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.json.JSONWithPadding;
import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.engine.CaptchaEngine;
import ru.terra.jbrss.engine.YandexCaptcha;
import ru.terra.server.controller.AbstractResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

@Path(URLConstants.DoJson.Captcha.CAPTCHA)
@Produces({"application/x-javascript", "application/json", "application/xml"})
public class CaptchaController extends AbstractResource {
    private CaptchaEngine captchaEngine = new YandexCaptcha();

    @GET
    @Path(URLConstants.DoJson.Captcha.CAP_GET)
    public JSONWithPadding get(@QueryParam("callback") String callback,
                               @Context HttpContext hc) {
        return new JSONWithPadding(captchaEngine.getCaptcha(), callback);
    }
}
