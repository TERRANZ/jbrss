package ru.terra.jbrss.shared.dto;

import java.util.ArrayList;
import java.util.List;

public class UserIdListDto {
    public List<UserIdDto> data = new ArrayList<>();

    public UserIdListDto() {
    }

    public UserIdListDto(List<UserIdDto> data) {
        this.data = data;
    }

    public List<UserIdDto> getData() {
        return data;
    }

    public void setData(List<UserIdDto> data) {
        this.data = data;
    }
}
