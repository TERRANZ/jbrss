package ru.terra.jbrss.engine;

import ru.terra.jbrss.dto.captcha.CaptchDTO;

public interface CaptchaEngine
{
	public CaptchDTO getCaptcha();

	public Boolean checkCaptcha(String val, String cid);
}
