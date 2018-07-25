package ru.terra.jbrss.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserIdListDto {
    public List<UserIdDto> data = new ArrayList<>();
}
