package ru.terra.jbrss.shared.dto;

import java.util.List;

public class FeedListDto {
    public List<FeedDto> data;

    public FeedListDto() {
    }

    public List<FeedDto> getData() {
        return data;
    }

    public void setData(List<FeedDto> data) {
        this.data = data;
    }
}
