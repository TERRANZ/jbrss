package ru.terra.jbrss.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedPostDto {
    public Integer id;
    public Integer feedId;
    public Long postdate;
    public String posttitle;
    public String postlink;
    public String posttext;
    public boolean isRead;
}
