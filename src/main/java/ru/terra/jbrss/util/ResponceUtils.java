package ru.terra.jbrss.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.terra.jbrss.dto.CommonDTO;
import flexjson.JSONSerializer;

public class ResponceUtils
{
	public static ResponseEntity<String> makeResponce(String json)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(json, headers, HttpStatus.OK);
	}

	public static ResponseEntity<String> makeResponce(CommonDTO commonDTO)
	{
		return makeResponce(new JSONSerializer().exclude("*.class").deepSerialize(commonDTO));
	}

	public static ResponseEntity<String> makeErrorResponce(String errorMessage, int errorCode)
	{
		CommonDTO ret = new CommonDTO();
		ret.errorCode = errorCode;
		ret.errorMessage = errorMessage;
		return makeResponce(ret);
	}

}
