package ru.terra.jbrss.dto;

import java.util.Date;

public class CommonDTO
{
	public String errorMessage = "";
	public Integer errorCode = 0;
	public String status = "";
	public Long timestamp = new Date().getTime();
}
