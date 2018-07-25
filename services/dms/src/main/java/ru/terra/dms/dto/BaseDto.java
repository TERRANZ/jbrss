package ru.terra.dms.dto;

import java.time.LocalDateTime;

public abstract class BaseDto {
    protected String id;
    protected LocalDateTime createDate, modifiedDate;

    public BaseDto(final String id, final LocalDateTime createDate, final LocalDateTime modifiedDate) {
        this.id = id;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
