package ru.terra.jbrss.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedPostsPageableDto {
    private List<FeedPostDto> posts;
    private Integer all;
}
