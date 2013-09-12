package ru.terra.jbrss.dto.rss;

import ru.terra.jbrss.dto.SimpleDataDTO;

public class SimpleIntDataDTO extends SimpleDataDTO<Integer> {

	public SimpleIntDataDTO(Integer data) {
		super(data);
	}

	public SimpleIntDataDTO() {
		super(0);
	}

}
