package ru.terra.dms.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DmsObjectDto extends BaseDto {

    private List<DmsObjectFieldDto> field;

    public DmsObjectDto(final String id, final LocalDateTime createDate, final LocalDateTime modifiedDate, final List<DmsObjectFieldDto> field) {
        super(id, createDate, modifiedDate);
        this.field = field;
    }

    public List<DmsObjectFieldDto> getField() {
        return field;
    }

    public void setField(final List<DmsObjectFieldDto> field) {
        this.field = field;
    }
}
