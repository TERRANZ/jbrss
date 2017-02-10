package ru.terra.jbrss.shared.dto;

public class BooleanDto {
    public Boolean status;

    public BooleanDto() {
    }

    public BooleanDto(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
