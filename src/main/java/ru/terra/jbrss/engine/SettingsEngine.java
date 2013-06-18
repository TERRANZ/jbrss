package ru.terra.jbrss.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Component;

import ru.terra.jbrss.db.controllers.SettingsJpaController;

@Singleton
@Component
public class SettingsEngine
{
	private SettingsJpaController sjpc;

	public SettingsEngine()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jbrss-dbPU");
		sjpc = new SettingsJpaController(emf);
		props = new Properties();
		try
		{
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE));

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			props = null;
		} catch (IOException e)
		{
			e.printStackTrace();
			props = null;
		}
	}

	private static String PROPERTIES_FILE = "server.properties";
	private Properties props;

	/**
	 * Получение настроек по ключу
	 * 
	 * @param settingsKey ключ настройки
	 * @param defaultValue возвращается если ключ в файле настроек не найден
	 * @return строка из файла настроек по запрошенному ключу
	 * */
	public String getPropertiesValue(String settingsKey, String defaultValue)
	{
		if (props != null)
		{
			return props.getProperty(settingsKey, defaultValue);
		}
		return defaultValue;
	}

	/**
	 * Установка значения по ключу в файл настроек
	 * 
	 * @param key ключ параметра
	 * @param value значение параметра
	 * @return предыдущее значение, либо null если значение было установлено первый раз
	 * */
	public void setPropertiesValue(String key, String value)
	{
		props.setProperty(key, value);
	}
}
