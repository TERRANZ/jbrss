package ru.terra.jbrss.shared.dto;

public class UserIdDto {
    public String id;

    public UserIdDto() {
    }

    public UserIdDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
