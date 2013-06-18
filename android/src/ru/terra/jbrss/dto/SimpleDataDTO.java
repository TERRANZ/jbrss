package ru.terra.jbrss.dto;

public class SimpleDataDTO<T> extends CommonDTO {
	public T data;

	public SimpleDataDTO() {
		data = null;
	}

	public SimpleDataDTO(T data) {
		this.data = data;
	}
}
