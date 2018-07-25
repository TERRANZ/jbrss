package ru.terra.dms.dto;

import java.time.LocalDateTime;

public class DmsObjectFieldDto extends BaseDto {
    private String dmsObjectId;
    private String type;
    private String name;
    private Integer intVal;
    private Long longVal;
    private String strVal;
    private Double floatVal;
    private Long dateVal;
    private String listVal;

    public DmsObjectFieldDto(final String id, final LocalDateTime createDate, final LocalDateTime modifiedDate, final String dmsObjectId, final String type, final String name) {
        super(id, createDate, modifiedDate);
        this.dmsObjectId = dmsObjectId;
        this.type = type;
        this.name = name;
    }

    public String getDmsObjectId() {
        return dmsObjectId;
    }

    public void setDmsObjectId(final String dmsObjectId) {
        this.dmsObjectId = dmsObjectId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getIntVal() {
        return intVal;
    }

    public void setIntVal(final Integer intVal) {
        this.intVal = intVal;
    }

    public Long getLongVal() {
        return longVal;
    }

    public void setLongVal(final Long longVal) {
        this.longVal = longVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(final String strVal) {
        this.strVal = strVal;
    }

    public Double getFloatVal() {
        return floatVal;
    }

    public void setFloatVal(final Double floatVal) {
        this.floatVal = floatVal;
    }

    public Long getDateVal() {
        return dateVal;
    }

    public void setDateVal(final Long dateVal) {
        this.dateVal = dateVal;
    }

    public String getListVal() {
        return listVal;
    }

    public void setListVal(final String listVal) {
        this.listVal = listVal;
    }
}
