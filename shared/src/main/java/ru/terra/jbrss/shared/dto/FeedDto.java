package ru.terra.jbrss.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedDto {
    public Integer id = 0;
    public String feedname = "";
    public String feedurl = "";
    public Long updateTime = 0L;
}
