package ru.terra.jbrss.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class FeedListDto {
    public List<FeedDto> data;
}

