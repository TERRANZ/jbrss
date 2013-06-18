package ru.terra.jbrss.engine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.terra.jbrss.constants.URLConstants;
import ru.terra.jbrss.dto.captcha.CaptchDTO;

@Component
public class YandexCaptcha implements CaptchaEngine
{
	private static final Logger logger = LoggerFactory.getLogger(YandexCaptcha.class);
	@Inject
	private SettingsEngine settingsEngine;

	@Override
	public CaptchDTO getCaptcha()
	{
		logger.info("getCaptcha()");
		String url = URLConstants.Yandex.CAPTCHA_GET_URL;
		// test yandex key
		url = url.replace(URLConstants.Yandex.PARAM_KEY, settingsEngine.getPropertiesValue("captcha.yandex.key",
				"cw.1.1.20110707T172051Z.faf547ce44f3d10b.d7e3028845ea04f56c38f7eef90999f765dd0d1f"));
		CaptchDTO ret = new CaptchDTO();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try
		{
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line = null;

			StringBuilder responseData = new StringBuilder();
			while ((line = in.readLine()) != null)
			{
				responseData.append(line);
			}
			in.close();
			String xml = responseData.toString();
			// hack to get valid xml from yandex
			xml = xml.substring(xml.indexOf("<get-captcha-result"));
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = db.parse(is);
			if (doc != null)
			{
				Node captchaNode = doc.getElementsByTagName("captcha").item(0);
				Node imageUrlNode = doc.getElementsByTagName("url").item(0);
				ret.cid = captchaNode.getTextContent();
				ret.image = imageUrlNode.getTextContent();

			}
		} catch (SAXException | IOException e)
		{
			e.printStackTrace();
		}
		logger.info("getCaptcha() return: " + ret.toString());
		return ret;
	}

	@Override
	public Boolean checkCaptcha(String val, String cid)
	{
		logger.info("checkCaptcha()");
		String url = URLConstants.Yandex.CAPTCHA_CHECK_URL;
		// test yandex key
		url = url.replace(URLConstants.Yandex.PARAM_KEY, settingsEngine.getPropertiesValue("captcha.yandex.key",
				"cw.1.1.20110707T172051Z.faf547ce44f3d10b.d7e3028845ea04f56c38f7eef90999f765dd0d1f"));
		url = url.replace(URLConstants.Yandex.PARAM_CID, cid);
		url = url.replace(URLConstants.Yandex.PARAM_CVAL, val);
		logger.info("checkCaptcha() result url : " + url);
		Boolean ret = false;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try
		{
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line = null;

			StringBuilder responseData = new StringBuilder();
			while ((line = in.readLine()) != null)
			{
				responseData.append(line);
			}
			in.close();
			String xml = responseData.toString();
			// hack to get valid xml from yandex
			xml = xml.substring(xml.indexOf("<check-captcha-result"));
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = db.parse(is);
			logger.info("result xml = " + xml);
			if (doc != null)
			{
				if (doc.getElementsByTagName("ok").getLength() > 0)
					ret = true;
				else
					ret = false;
			}
		} catch (SAXException | IOException e)
		{
			e.printStackTrace();
		}
		logger.info("checkCaptcha() result: " + ret);
		return ret;
	}

}
